package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// Spring Security 의 UsernamePasswordAuthenticationFilter 가 있다
// /login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 가 동작한다.
// 로그인을 진행하는 필터이기 때문에 AuthenticationManager 를 통해서 로그인을 진행해야 한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도");

        // 1. username, password 를 받고
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            // password 는 스프링 내부적으로 처리해준다
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService 의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication 이 리턴된다
            // DB에 있는 username 과 password 가 일치한다
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 인증이 정상적으로 완료 시
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); // Object 를 리턴하기 때문에 Cast 필요
            System.out.println("로그인 인증 완료?: "+principalDetails.getUser().getUsername()); // 로그인 정상적으로 되었다는 뜻

            // return 되면서 Authentication 객체가 Session 영역에 저장된다 => 로그인이 되었다는 뜻
            // authentication 객체가 session 영역에 저장을 해야하고 그 방법이 return 을 해주기만 하면 된다
            // 리턴의 이유는 권한 관리를 security 가 대신 해주기 때문에 편하다
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없다. 하지만 단지 권한 처리때문에 session 에 넣어 준다

            return authentication;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. 유효한지 확인 authenticationManager 로 로그인 시도를 하면 
        // PrincipalDetailsService 가 호출되고 loadUserByUsername() 함수가 실행된다

        // 3. PrincipalDetails 를 세션에 담고 ( 권한 관리를 위해서 )
        // 4. JWT 토큰을 만들어서 응답해주면 된다
//       return super.attemptAuthentication(request, response);
        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 된다
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행된다 ");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA 방식이 아닌 Hash 암호방식
        String jwtToken = JWT.create()
//                .withSubject(principalDetails.getUsername())
                .withSubject("kay_token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
                .withClaim("id", principalDetails.getUser().getId()) // 비공개 claim 으로 넣고 싶은 값을 넣을 수 있다
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.HEADER+jwtToken);
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}