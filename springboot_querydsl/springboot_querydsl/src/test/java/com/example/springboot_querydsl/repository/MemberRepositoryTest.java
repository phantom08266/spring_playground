package com.example.springboot_querydsl.repository;

import com.example.springboot_querydsl.dto.MemberSearchCondition;
import com.example.springboot_querydsl.dto.MemberTeamDto;
import com.example.springboot_querydsl.entity.Member;
import com.example.springboot_querydsl.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
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

    @Autowired
    EntityManager em;
    private void setUp()  {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void memberSearchBuilderTest() {
        setUp();

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setUsername("member1");
        condition.setAgeLoe(30);

        List<MemberTeamDto> members = memberQueryDslRepository.searchByBuilder(condition);

        for (MemberTeamDto member : members) {
            System.out.println("member = " + member);
        }
        assertThat(members).extracting("username")
                .containsExactly("member1");
    }

    @Test
    void memberSearchWhereTest() {
        setUp();

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setUsername("member1");
        condition.setAgeLoe(30);

        List<MemberTeamDto> members = memberQueryDslRepository.searchByWhere(condition);

        for (MemberTeamDto member : members) {
            System.out.println("member = " + member);
        }
        assertThat(members).extracting("username")
                .containsExactly("member1");
    }
}