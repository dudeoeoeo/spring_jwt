package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        // 토큰: id, pw 를 받아서 로그인이 완료되면 토큰을 만들어서 response
        // 요청할 때마다 header에 Authorization에 value 값으로 토큰을 가지고 오면
        // 토큰을 확인하고 유효한 토큰인지를 확인 ( RSA, HS256 )
        if(req.getMethod().equals("POST")) {
            System.out.println("필터3" + req.toString() + " // " + res.toString());
            System.out.println("POST 요청 됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("header: "+headerAuth);

            if (checkValidToken(headerAuth)) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
            }
        }
//        chain.doFilter(req, res); // 여기를 거쳐서 계속 진행할 수 있도록 chain 에 request, response를 넘겨준다
    }

    public boolean checkValidToken(String token) {
        if(token.equals("kay")) {
            return true;
        }
        return false;
    }
}
