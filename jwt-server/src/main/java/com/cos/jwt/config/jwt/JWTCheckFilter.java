package com.cos.jwt.config.jwt;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JWTUtil jwtUtil) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        String token = request.getHeader("Authentication");
        if (token == null || !token.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        VerifyResult result = jwtUtil.verify(token.replace("Bearer ", ""));
        if (result.isResult()) {
            User user = userRepository.findById(Long.valueOf(result.getUserId())).orElseThrow(() -> new UsernameNotFoundException("해당 유저가 없습니다."));
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user.getRoleList(), user, null)
            );
        }
        chain.doFilter(request, response);
    }
}
