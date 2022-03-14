package com.cos.jwt.controller;

import com.cos.jwt.config.jwt.JwtProperties;
import com.cos.jwt.config.jwt.JwtTokenProvider;
import com.cos.jwt.dto.SignInDto;
import com.cos.jwt.dto.SignUpDto;
import com.cos.jwt.model.CommonDto;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signUp")
    public CommonDto<?> signUp(@RequestBody SignUpDto signUpDto, BindingResult bindingResult) {
        User newUser = User.signUpUser(signUpDto, passwordEncoder);
        userRepository.save(newUser);
        return new CommonDto<>(HttpStatus.OK.value(), "회원가입에 성공했습니다.");
    }

    @PostMapping("/signIn")
    public CommonDto<?> signIn(@RequestBody SignInDto signInDto, BindingResult bindingResult) {
        User savedUser = userRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. " + signInDto.getUsername()));
        if (!passwordEncoder.matches(signInDto.getPassword(), savedUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 확인해 주세요.");
        }
        return new CommonDto<>(HttpStatus.OK.value(), "로그인 되었습니다.");
    }

    @GetMapping("/refresh-token")
    public CommonDto<?> refreshToken(HttpServletRequest request) {
        Long userId = jwtTokenProvider.getUserId(request.getHeader(JwtProperties.HEADER));
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못했습니다. " + userId));
        String accessToken = jwtTokenProvider.renewAccessToken(user);
        return new CommonDto<>(HttpStatus.OK.value(), accessToken);
    }
}
