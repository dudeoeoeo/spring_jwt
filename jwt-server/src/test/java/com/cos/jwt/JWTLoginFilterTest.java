package com.cos.jwt;

import com.cos.jwt.constant.Authority;
import com.cos.jwt.dto.UserLogin;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JWTLoginFilterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RestTemplate restTemplate = new RestTemplate();

    private URI uri(String path) throws URISyntaxException {
        return new URI(format("http://localhost:%d%s", port, path));
    }

    @BeforeEach
    void before() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("user@test.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRoles(String.valueOf(Authority.USER));

        User admin = new User();
        admin.setUsername("admin@test.com");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setRoles(String.valueOf(Authority.ADMIN));

        userRepository.save(user);
        userRepository.save(admin);
    }

    @DisplayName("1. JWT로 로그인을 시도한다.")
    @Test
    void JWT_로그인_시도() throws URISyntaxException {
        UserLogin userLogin = UserLogin.builder().username("user@test.com").password("1234").build();
        HttpEntity<UserLogin> body = new HttpEntity<>(userLogin);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);

        assertEquals(200, responseEntity.getStatusCodeValue());

        System.out.println(responseEntity.getHeaders().get("Authentication"));
    }

    private String getToken(String username, String password) throws URISyntaxException {
        UserLogin userLogin = UserLogin.builder().username(username).password(password).build();

        HttpEntity<UserLogin> body = new HttpEntity<>(userLogin);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);

        return responseEntity.getHeaders().get("Authentication").get(0).replace("Bearer ", "");
    }

    @DisplayName("admin 테스트")
    @Test
    void 어드민_권한_테스트() throws URISyntaxException {
        String accessToken = getToken("admin@test.com", "1234");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(uri("/api/v1/admin/userList"), HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());
    }
}
