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
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberSpringDataJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberSpringDataJpaRepository memberSpringDataJpaRepository;


    void setUp() {
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
    void memberTest() {
        Member tomo = new Member("tomo", 12);
        memberSpringDataJpaRepository.save(tomo);

        Member findByMember = memberSpringDataJpaRepository.findById(tomo.getId()).get();
        assertThat(findByMember).isEqualTo(tomo);

        List<Member> members = memberSpringDataJpaRepository.findAll();
        assertThat(members).containsExactly(tomo);

        List<Member> byUsername = memberSpringDataJpaRepository.findByUsername(tomo.getUsername());
        assertThat(byUsername).containsExactly(tomo);
    }

    @Test
    void memberSearchTest() {
        setUp();
        MemberSearchCondition condition = new MemberSearchCondition();
//        condition.setUsername("member1");
        condition.setAgeGoe(20);
        condition.setAgeLoe(30);
        List<MemberTeamDto> results = memberSpringDataJpaRepository.search(condition);
        assertThat(results)
                .extracting("username")
                .containsExactly("member2", "member3");
    }
}