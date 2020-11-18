package com.example.Memo.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.Memo.config.auth.CustomSecurityMember;
import com.example.Memo.config.auth.jwt.JwtTokenUtil;
import com.example.Memo.config.auth.jwt.JwtUserDetails;
import com.example.Memo.exception.ObjectNonexistentException;
import com.example.Memo.model.member.Member;
import com.example.Memo.service.MemberService;
import com.example.Memo.util.MailComponent;
import com.example.Memo.util.RegexUtil;
import com.example.Memo.util.RequestUtil;
import com.example.Memo.web.dto.ResponseDto;
import com.example.Memo.web.dto.auth.JwtRequestDto;
import com.example.Memo.web.dto.auth.JwtResponseDto;
import com.example.Memo.web.dto.member.MemberJoinRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 회원가입 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  @Value("${join.secret}")
  private String secret;

  private final MemberService memberService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final PasswordEncoder passwordEncoder;
  private final MailComponent mailComponent;

  /**
   * 일반 email + password 회원가입 api
   */
  @PostMapping("/member/join")
  public ResponseEntity<ResponseDto> join(@Valid @RequestBody MemberJoinRequestDto params) {
    log.info("member join invoked. requestDto. username:" + params.getEmail());
    params.checkJoinUser(secret); // merchant, deliver, customer 입력 데이터 검사
    ResponseDto responseDto = ResponseDto.makeSuccessResponseStatus();
    HttpStatus responseStatus = HttpStatus.OK;
    try {
      Member member = memberService.join(params);
      log.info("member join Success, memberId:" + member.getId() + ", username:" + params.getEmail());
    } catch (DataIntegrityViolationException e) {
      log.error("member join DataIntegrityViolationException:" + e.getMessage());
      responseDto = ResponseDto.builder()
              .msg(RequestUtil.MEMBER_EMAIL_EXIST_MSG)
              .code(RequestUtil.MEMBER_EMAIL_EXIST_CODE)
              .build();
      responseStatus = HttpStatus.BAD_REQUEST;
    }
    return new ResponseEntity<>(responseDto, responseStatus);
  }

  /**
   * 이메일 중복 체크 api
   */
  @PostMapping("/member/check-email")
  public ResponseEntity<ResponseDto> checkEmail(@RequestBody JSONObject params) {
    log.info("member checkEmail invoked. email:" + params.get("email"));
    String email = params.get("email").toString();
    if (!RegexUtil.isValidEmail(email)) {
      return new ResponseEntity<>(ResponseDto.builder()
              .msg(RequestUtil.MEMBER_EMAIL_UNAVAILABLE_MSG)
              .code(RequestUtil.MEMBER_EMAIL_UNAVAILABLE_CODE)
              .build(),
              HttpStatus.BAD_REQUEST);
    }
    try {
      // 이미 이메일 정보가 있을경우 존재한다는 에러 반환
      Member member = memberService.findMemberByUserName(email);
      return new ResponseEntity<>(ResponseDto.builder()
              .msg(RequestUtil.MEMBER_EMAIL_EXIST_MSG)
              .code(RequestUtil.MEMBER_EMAIL_EXIST_CODE)
              .build(),
              HttpStatus.BAD_REQUEST);
    } catch (ObjectNonexistentException e) {
      // 기존 이메일이 없다면 성공 반환
      return new ResponseEntity<>(ResponseDto.makeSuccessResponseStatus(), HttpStatus.OK);
    }
  }

  /**
   * 로그인
   */
  @PostMapping("/member/login")
  public ResponseEntity<ResponseDto> authenticate(@Valid @RequestBody JwtRequestDto params) {
    log.info("authenticate invoked. username:" + params.getUsername());
    String username = params.getUsername();
    String password = params.getPassword();

    ResponseDto responseDto = null;
    HttpStatus httpStatus = HttpStatus.OK;
    try {
      // username, password 를 사용해 인증토큰 생성, JwtUserDetailsService 사용
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
      // 인증 및 토큰정보 생성
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      CustomSecurityMember userDetails = (CustomSecurityMember) authentication.getPrincipal();

      responseDto = new JwtResponseDto(jwtTokenUtil.generateToken(userDetails), userDetails.getMember().getRole(), LocalDateTime.now());
    } catch (DisabledException e) { // 비활성된 계정
      log.error("authenticate userinfo DisabledException, username:" + username + ", Error:" + e.getMessage());
      responseDto = ResponseDto.builder()
              .msg(RequestUtil.MEMBER_ACCOUNT_DISABLE_MSG)
              .code(RequestUtil.MEMBER_ACCOUNT_DISABLE_CODE)
              .build();
      httpStatus = HttpStatus.BAD_REQUEST;
    } catch (InternalAuthenticationServiceException | BadCredentialsException e) { // 비밀번호 불일치, oauth 로 인해 비밀번호가 null일경우 InternalAuthenticationServiceException 반환
      log.error("authenticate userinfo BadCredentialsException, username:" + username + ", Error:" + e.getMessage());
      responseDto = ResponseDto.builder()
              .msg(RequestUtil.MEMBER_LOGIN_INFO_INVALID_MSG)
              .code(RequestUtil.MEMBER_LOGIN_INFO_INVALID_CODE)
              .build();
      httpStatus = HttpStatus.BAD_REQUEST;
    } catch (CredentialsExpiredException e) { // 사용자 기간 만료, 로그인은 허용하지만 만료 안내
      log.warn("authenticate userinfo CredentialsExpiredException, username:" + username + ", Error:" + e.getMessage());

      Member member = memberService.findMemberByUserName(username);
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(member.getRole().getKey()));
      String token = jwtTokenUtil.generateToken(new JwtUserDetails(
              member.getId(),
              member.getUserName(),
              member.getPassword(),
              member.getRole(),
              authorities));

      responseDto = new JwtResponseDto(
              RequestUtil.MEMBER_PASSWORD_EXPIRED_MSG,
              RequestUtil.REQUEST_SUCCESS_CODE,
              token,
              member.getRole(),
              LocalDateTime.now());
      httpStatus = HttpStatus.OK;
    }
    return new ResponseEntity<>(responseDto, httpStatus);

  }

  @PostMapping("/member/reset-password")
  public ResponseDto resetPassword(@RequestBody JSONObject params) {
    log.info("resetPassword invoked. username:" + params.get("email"));
    String email = (String) params.get("email");
    Member member = memberService.findMemberByUserName(email);
    String plainPassword = RandomStringUtils.randomAlphanumeric(10);
    String encryptPassword = passwordEncoder.encode(plainPassword);
    memberService.saveMember(member);
    mailComponent.sendEmail(email,
            "beydrologis 비밀번호 변경 안내",
            "아래 비밀번호로 로그인하시기 바랍니다.\n" + plainPassword);
    return ResponseDto.makeSuccessResponseStatus();
  }
}

