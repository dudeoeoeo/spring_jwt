package com.cos.jwt.controller;

import com.cos.jwt.model.CommonDto;
import com.cos.jwt.model.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/m")
@RestController
public class MemberController {

    @CrossOrigin
    @GetMapping("/member")
    public ResponseEntity<CommonDto<List<Member>>> findAll() {
        System.out.println("findAll()");
        Member member1 = new Member(1, "test1", "1234", "01011112222");
        Member member2 = new Member(2, "test2", "1234", "01011112222");
        Member member3 = new Member(3, "test3", "1234", "01011112222");
        List<Member> list = new ArrayList<>();
        list.add(member1);
        list.add(member2);
        list.add(member3);
        return new ResponseEntity<>(new CommonDto<>(HttpStatus.OK.value(), list), HttpStatus.OK);
    } // MessageConverter (JavaObject -> Json String)

    @CrossOrigin
    @GetMapping("/member/{id}")
    public CommonDto<Member> findMember(@PathVariable int id) {
        System.out.println("findMember()");
        Member member1 = new Member(1, "test1", "1234", "01011112222");
        return new CommonDto<>(HttpStatus.OK.value(), member1);
//        return new ResponseEntity<>(new CommonDto<>(HttpStatus.OK.value(), member1), HttpStatus.OK);
    }

    @PostMapping("/member")
    // x-www-form-urlencoded (request.getParameter()) String username, String password, String phone
    // text/plain => @RequestBody 어노테이션
    // application/json => @RequestBody 어노테이션 + 오브젝트
    public void save(@RequestBody Member member) {

    }

    @DeleteMapping("/member/{id}")
    public void delete(@PathVariable int id) {

    }

    @PutMapping("/user/{id}")
    public void update(@PathVariable int id,String password, String phone) {

    }
}
