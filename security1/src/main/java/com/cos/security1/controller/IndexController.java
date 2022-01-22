package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserReposiry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserReposiry userReposiry;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) 생략 가능
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 설정을 하지 않으면 스프링 시큐리티가 낚아챔
    @GetMapping("/loginForm")
    public String loginForm() {
        System.out.println("loginForm");
        return "loginForm";
    }

    @GetMapping("joinForm")
    public String joinForm() {
        System.out.println("joinForm");
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println("user: " + user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userReposiry.save(user); // 패스워드를 암호화하지 않으면 시큐리티 로그인을 할 수 없다
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // data 메서드가 실행되기 직전에 실행된다
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터";
    }
}
