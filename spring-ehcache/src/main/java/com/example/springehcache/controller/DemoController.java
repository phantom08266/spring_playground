package com.example.springehcache.controller;

import com.example.springehcache.entity.Member;
import com.example.springehcache.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DemoController {

    private final MemberService memberService;

    @GetMapping("/")
    public List<Member> getMembers() {
        return memberService.allMember(1L);
    }
}
