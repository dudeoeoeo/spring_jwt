package com.cos.jwt.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터2" + request.toString() + " // " + response.toString());
        chain.doFilter(request, response); // 여기를 거쳐서 계속 진행할 수 있도록 chain 에 request, response를 넘겨준다
    }
}
