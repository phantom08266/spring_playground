package com.example.springboot_querydsl;

import com.example.springboot_querydsl.entity.Member;
import com.example.springboot_querydsl.entity.QMember;
import com.example.springboot_querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QueryDslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory query;
    @BeforeEach
    void setUp() {
        query = new JPAQueryFactory(em); // 멀티스레드 환경에서 사용할 할 수 있도록 트랜젝션 단위로 바인딩 될 수 있도록 설계되어있다.

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

        em.flush();
        em.clear();

        List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : resultList) {
            System.out.println(member);
            System.out.println(member.getTeam());
        }
    }

    @Test
    void jpqlTest() {
        Member findMember = em.createQuery("select m from Member m " +
                        "where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test
    void queryDslTest() {
        QMember m = new QMember("m");
        Member findMember = query
                .select(m)
                .from(m)
                .where(m.username.eq("member1")) // 파라미터 바인딩 처리가 되어있어 SQL INJECTION 공격을 방어할 수 있다.
                .fetchOne();

        assertThat(findMember.getAge()).isEqualTo(10);
    }
}
