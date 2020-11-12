package com.example.Memo.config.auth.jwt;

import com.example.Memo.config.auth.CustomSecurityMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * jwt token 을 다루기위한 유틸, jwt 생성, 사용자 정보 가져오기, 유효시간 확인
 */
@Component
public class JwtTokenUtil {
    public static final int JWT_TOKEN_VALIDITY = 5; //5분
    private String secretKey = "beyless1!";
    SecretKey key;
    {
        byte[] keyArray = new byte[64];
        for (int i = 0; i < secretKey.length(); i++) {
            keyArray[i] = (byte) secretKey.charAt(i);
        }
        key = Keys.hmacShaKeyFor(keyArray);
    }

    //jwt 토큰으로 부터 username 가져옴
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //jwt 토큰으로 부터  expiration date 가져옴
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Claims expiration date 혹은 username 를 가져오기 위해 호출
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 토큰의 내부 데이터를 복호화 하여 찾아오기 위해 시크릿 키가 필요
    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 시간 초과 확인
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof CustomSecurityMember) {
            claims.put("id", ((CustomSecurityMember) userDetails).getMember().getId());
        } else if (userDetails instanceof JwtUserDetails) {
            claims.put("id", ((JwtUserDetails) userDetails).getMember().getId());
        }
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            claims.put("role", authority.getAuthority());
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String refreshToken(String token) {
        return token;
    }

    //1. 발급자, 만료, 주제 및 ID와 같은 토큰의 클레임을 정의하십시오.
    //2. HS512 알고리즘과 비밀 키를 사용하여 JWT에 서명하십시오.
    //3. JWS Compact Serialization에 따르면 (https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // URL-safe string 로 JWT 압축한다.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(DateUtils.addMinutes(now, JWT_TOKEN_VALIDITY * 1000 * 10)) // 5만분
                .signWith(key)
                .compact(); //3
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Map<String, Object> getUserParseInfo(String token) {
        Claims parseInfo = getAllClaimsFromToken(token);
        Map<String, Object> result = new HashMap<>();
        result.put("username", parseInfo.getSubject());
        result.put("id", parseInfo.get("id", Long.class));
        result.put("role", parseInfo.get("role", String.class));
        return result;
    }


    //test 용 시간제한 없는 jwt 토큰
    public String generateUltimateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof CustomSecurityMember) {
            claims.put("id", ((CustomSecurityMember) userDetails).getMember().getId());
        } else if (userDetails instanceof JwtUserDetails) {
            claims.put("id", ((JwtUserDetails) userDetails).getMember().getId());
        }
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            claims.put("role", authority.getAuthority());
        }
        return doGenerateUltimateToken(claims, userDetails.getUsername());
    }

    //test 용 시간제한 없는 jwt 토큰
    private String doGenerateUltimateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(DateUtils.addMinutes(now, JWT_TOKEN_VALIDITY * 1000 * 10)) // 5만분
                .signWith(key)
                .compact(); //3
    }
}