package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository 라는 어노테이션이 없어도 IoC (Inversion of Control) 가 된다 => JpaRepository 를 상속했기 때문에 가능
public interface UserReposiry extends JpaRepository<User, Long> {

    public User findByUsername(String username);
}
