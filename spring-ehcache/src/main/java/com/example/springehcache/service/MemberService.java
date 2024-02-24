package com.example.springehcache.service;

import com.example.springehcache.entity.Member;
import com.example.springehcache.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Cacheable(cacheNames = "allMember", key = "#id")
    public List<Member> allMember(Long id) {
        return memberRepository.findAll();
    }
}
