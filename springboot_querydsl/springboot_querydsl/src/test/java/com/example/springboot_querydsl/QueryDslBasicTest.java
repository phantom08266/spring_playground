package com.example.springboot_querydsl;

import com.example.springboot_querydsl.entity.Member;
import com.example.springboot_querydsl.entity.QMember;
import com.example.springboot_querydsl.entity.QTeam;
import com.example.springboot_querydsl.entity.Team;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.springboot_querydsl.entity.QMember.member;
import static com.example.springboot_querydsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.*;
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

    @Test
    void aliasTest() {
        QMember m = member; // 이미 만들어진 객체를 사용할 수 있다. JPQL쿼리를 보면 member1으로 alias되는 것을 확인할 수 있다.
        // 따라서 같은 테이블을 조인할때에는 alias를 다르게 설정하기 위해서만 new QMember("m")을 사용하고 나머지는 static import를 사용하는 것이 깔끔하다.
        Member findMember = query
                .select(m)
                .from(m)
                .where(m.username.eq("member1")) // 파라미터 바인딩 처리가 되어있어 SQL INJECTION 공격을 방어할 수 있다.
                .fetchOne();
    }

    @Test
    void basicQueryTest() {
        List<Member> findMembers = query
                .select(member)
                .from(member)
                .where(
                        member.username.ne("member1"), // .and를 사용하지 않아도 모두 and 조건을 사용할 경우 이렇게 컴마(,)로도 처리할 수 있다.
                        member.age.between(10, 30)
                ).fetch();

        assertThat(findMembers).hasSize(2);
    }

    @Test
    void resultFetchTest() {
        // querydsl 5.0.0부터는 fetchCount, fetchResults가 deprecated되었다. 이유는 복잡한 쿼리일 경우 정확한 결과를 반환하지 않는다고 한다.
        // 따라서 직접 쿼리를 작성해서 별도의 count 등을 구하는 것이 좋다. 페이징 처리를 할때 사용하려고 했는데 deprecated되었기 때문에 사용하지 않는 것이 좋겠다.
        query.selectFrom(member)
                .fetchCount();

        query.selectFrom(member)
                .fetchResults();

        // List를 반환한다. 주로 많이 사용할 것 같음.
        List<Member> fetch = query.selectFrom(member)
                .fetch();

        // 단건을 반환하며, 없으면 null을 반환하고 2개 이상이면 NonUniqueResultException이 발생된다.
        query.selectFrom(member)
                .where(member.age.goe(40))
                .fetchOne();

        // limit(1).fetchOne()과 같다.
        query.selectFrom(member)
                .fetchFirst();
    }

    @Test
    void sortTest() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 105));
        em.persist(new Member("member6", 106));

        List<Member> findMembers = query.selectFrom(member)
                .where(member.age.goe(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member6 = findMembers.get(0);
        Member member5 = findMembers.get(1);
        Member memberNull = findMembers.get(2);

        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    void pagingTest() {
        // JPQL에서는 페이징 처리하는 쿼리를 볼 수 없지만 실제 쿼리부분을 보면 정상적으로 페이징 쿼리가 날라가는 것을 확인할 수 있다.
        List<Member> findMembers = query.selectFrom(member)
                .orderBy(member.age.desc()) // 당연히 페이징 처리를 하기 위해서는 정렬을 먼저 시킨 후 처리해야 한다.
                .offset(1) // 몇번째 부터 시작할 것인지
                .limit(2) // 몇개를 가져올 것인지
                .fetch();

        assertThat((findMembers)).hasSize(2);
    }

    @Test
    void aggregationTest() {
        List<Tuple> findMembers = query.select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.min(),
                        member.age.max()
                )
                .from(member)
                .fetch();

        Tuple tuple = findMembers.get(0);

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    // 팀의 이름과 각 팀의 평균 연령을 구해라.
    @Test
    void aggregationTest2() {
        List<Tuple> findMember = query
                .select(
                        team.name,
                        member.age.avg()
                )
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = findMember.get(0);
        Tuple teamB = findMember.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    @Test
    void joinTest() {
        List<Member> findMembers = query
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(findMembers).hasSize(2);
        assertThat(findMembers)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀 이륾과 같은 회원 조회
     */
    @Test
    void thetaJoinTest() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        // from 절에 여러 엔티티를 선택해서 세타 조인을 사용할 수 있다. 하지만 이럴경우 Left Joint을 같이 사용할 수 없기때문에 on절을 사용하는 것이 좋다.
        List<Member> result = query
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }
}
