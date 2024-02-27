package com.example.springehcache.service;

import com.example.springehcache.entity.Member;
import com.example.springehcache.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Cacheable(cacheNames = "allMember", key = "'allMembersKey'")
    public List<Member> allMember() {
        return memberRepository.findAll();
    }

    @Transactional
    @Cacheable(cacheNames = "pageMember", key = "'pageMembersKey-'+#page")
    public List<Member> pageMember(int page) {
        final int pageSize = 10;
        int pageNumber = (page -1) * pageSize;
//        log.info("pageNumber: {}", pageNumber);
        return memberRepository.findAllByMemberWithPagination(PageRequest.of(pageNumber, pageSize));
    }
}
