package com.example.springboot_querydsl.repository;

import com.example.springboot_querydsl.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    MemberQueryDslRepository memberQueryDslRepository;


    @Test
    void memberJPATest() {
        Member tomo = new Member("tomo", 12);
        memberJpaRepository.save(tomo);

        Member findByMember = memberJpaRepository.findById(tomo.getId());
        assertThat(findByMember).isEqualTo(tomo);

        List<Member> members = memberJpaRepository.findAll();
        assertThat(members).containsExactly(tomo);

        List<Member> results = memberJpaRepository.findByUsername(tomo.getUsername());
        assertThat(results).containsExactly(tomo);
    }

    @Test
    void memberQueryDslTest() {
        Member tomo = new Member("tomo", 12);
        memberQueryDslRepository.save(tomo);

        Member findByMember = memberQueryDslRepository.findById(tomo.getId());
        assertThat(findByMember).isEqualTo(tomo);

        List<Member> members = memberQueryDslRepository.findAll();
        assertThat(members).containsExactly(tomo);

        List<Member> byUsername = memberQueryDslRepository.findByUsername(tomo.getUsername());
        assertThat(byUsername).containsExactly(tomo);
    }
}