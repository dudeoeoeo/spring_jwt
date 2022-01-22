package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserReposiry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행된다
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserReposiry userReposiry;

    // 시큐리티 session = Authentication (리턴된 값이 Authentication 내부에 UserDetails 가 들어간다) = UserDetails
    // Security Session(Authentication(UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername " + username);
        User userEntity = userReposiry.findByUsername(username);
        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
