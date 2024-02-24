package com.example.springboot_querydsl;

import com.example.springboot_querydsl.entity.Member;
import com.example.springboot_querydsl.entity.QMember;
import com.example.springboot_querydsl.entity.QTeam;
import com.example.springboot_querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static com.example.springboot_querydsl.entity.QMember.*;
import static com.example.springboot_querydsl.entity.QTeam.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TempTestRepositoryTest {

    @Autowired
    private EntityManager em;

    private JPAQueryFactory query;

    @BeforeEach
    void setUp() {
        query = new JPAQueryFactory(em);

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
    void member1에_속한_팀을_조회한다() {
        Member result = query.select(member)
                .from(member)
                .where(member.id.eq(1L))
                .fetchFirst();
        assertThat(result.getTeam().getId()).isEqualTo(1L);
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    void team1에_유저가_이미_존재하는지_체크한다() {
//        boolean result = query.selectFrom(team)
//                .where(team.id.eq(1L),
//                        team.members.isEmpty()
//                )
//                .fetch()
//                .isEmpty();

//        Team team = query.selectFrom(QTeam.team)
//                .where(QTeam.team.id.eq(1L))
//                .fetchFirst();
//
//        assertThat(team.getMembers()).isNotEmpty();

        Team team = query.selectFrom(QTeam.team)
                .leftJoin(QTeam.team.members, member).fetchJoin()
                .where(QTeam.team.id.eq(1L))
                .fetchFirst();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(team.getMembers());
        assertThat(loaded).isTrue();
        assertThat(team.getMembers()).isNotEmpty();
    }

    @Test
    void test3() {
        Boolean result = query.select(member.isNull())
                .from(member)
                .where(member.username.eq("member5"))
                .fetchFirst();

        assertThat(result).isTrue();
    }
}
