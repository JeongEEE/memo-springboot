package com.example.Memo.config;

import com.example.Memo.config.auth.jwt.JwtRequestFilter;
import com.example.Memo.model.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 다른 클래스에서도 Security 관련 어노테이션사용 가능
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }


  /**
   * @Component 로 등록된 JwtRequestFilter 가 일반 서블릿 필터로 중복 등록되는것을 방지
   */
  @Bean
  public FilterRegistrationBean registration(JwtRequestFilter filter) {
    FilterRegistrationBean registration = new FilterRegistrationBean(filter);
    registration.setEnabled(false);
    return registration;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    super.configure(auth);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .httpBasic().and().csrf().disable();
    http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
            .and()
            .authorizeRequests() //url 별 권한관리 시작점
            //.antMatchers("/member/api/v1/**", "/member/api/v1/authenticate", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
            .antMatchers("/guest/**").hasAnyRole(Role.GUEST.name(), Role.ADMIN.name())
            .antMatchers("/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
            .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
            .anyRequest().authenticated() // 위의 url 외에는 모두 인증된 사용자는 가능
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
    //.oauth2Login() //oAuth 설정 진입점
    //.userInfoEndpoint() // oAuth 로그인 성공후 사용자 정보 가져오는 설정
    //.userService(customOAuth2UserService); //로그인 성공후 후속조치를 위한 OAuth2UserService 구현체, 리소스 서버로부터 사용자 정보를 가져온 후 추가진행
    ;
  }

  /**
   * anyRequest().authenticated() 로 인해 404 코드 대신 401 코드가 반환되는 건에 대해
   * 실제 404, 500 등의 에외가 발생하면 default servlet filter 에 의해 /error url 로 redirect
   * 된다, 따라서 아래 ignoring 에 /error 도 포함시켜 주어야 정상적으로 404가 반환된다.
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(
            "/static/**"
            , "/get-board/**"
            , "/swagger-ui.html"
            , "/swagger**"
            , "/swagger-resources/**"
            , "/webjars/**"
            , "/error"
            , "/member/**"
            , "/oauth2/**"
            , "/monitor/**"
            , "/css/**"
            , "/images/**"
            , "/js/**"
            , "/h2-console/**"
            , "/profile"
            , "/websocket/**"
            , "/payment/**"
    );
  }
}