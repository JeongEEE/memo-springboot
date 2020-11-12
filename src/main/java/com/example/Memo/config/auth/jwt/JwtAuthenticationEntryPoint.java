package com.example.Memo.config.auth.jwt;

/**
 * 인증되지 않았을 경우(jwtFilter에서 security context 에 인증정보 없음)
 * 아래 error 를 반환하도록 설정
 */
//@Slf4j
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//    }
//}
