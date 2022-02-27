//package com.cos.jwt.filter;
//
//import com.auth0.jwt.JWT;
//import com.cos.jwt.config.jwt.JwtProperties;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//
//public class JwtCustomBeforeFilter extends BasicAuthenticationFilter {
//
//    private JWT jwt;
//
//    public JwtCustomBeforeFilter(AuthenticationManager authenticationManager, JWT jwt) {
//        super(authenticationManager);
//        this.jwt = jwt;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
////        super.doFilterInternal(request, response, chain);
//        System.out.println("JwtCustomBeforeFilter called");
//        String jwtToken = getAuthenticationToken(request);
//
//        if( jwtToken != null ) {
//
//        }
//    }
//
//    private String getAuthenticationToken(HttpServletRequest request) {
//        String token = request.getHeader(JwtProperties.HEADER_STRING);
//        if( token != null) {
//            System.out.println("[JwtCustomBeforeFilter] token: " + token);
//
//            try {
//                token = URLDecoder.decode(token, "UTF-8");
//                if (!token.startsWith(JwtProperties.HEADER))
//                    throw new IllegalArgumentException("Token is not valid");
//
//                return token.replace(JwtProperties.HEADER, "");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//}
