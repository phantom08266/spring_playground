package com.example.springehcache.controller;

import com.example.springehcache.entity.Member;
import com.example.springehcache.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DemoController {

    private final MemberService memberService;

    @GetMapping("/members")
    public List<Member> getMembers() {
        return memberService.allMember();
    }

    @GetMapping("/page/members/{page}")
    public List<Member> getPageMembers(@PathVariable int page) {
        return memberService.pageMember(page);
    }
}
