package com.example.springboot_querydsl;

import com.example.springboot_querydsl.dto.QUserDto5;
import com.example.springboot_querydsl.dto.UserDto5;
import com.example.springboot_querydsl.entity.Member;
import com.example.springboot_querydsl.entity.QMember;
import com.example.springboot_querydsl.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.tsv.TsvFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.Objects;

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
        // join, leftJoin, rightJoin 사용할 수 있다.
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

    /**
     * on절을 사용한 조인 방법(JPA 2.1부터 지원함)
     * 1. 조인 대상을 필터링할때 사용한다.
     * 2. 연관관계 없는 엔티티 외부 조인
     */
    @Test
    void onJoinTest() {
//        회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
        List<Tuple> results = query
                .select(member, team) // select절에 여러 엔티티를 선택하면 Tuple로 반환한다.
                .from(member)
                .leftJoin(member.team, team) // leftJoin을 사용하기 때문에 on절을 사용한 것임. 만약 teamA에 속한 회원만 조회를 원한다면 innerJoin을 사용해서 where절에 조건을 다는것도 좋은 방법이다.
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void onJoinTest2() {
        List<Tuple> results = query
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void onJoinNoRelationTest() {
        // 회원의 이름과 팀 이름이 같은 대상 외부조인
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        // join절을 보면 일반 조인과 다르게 하나의 엔티티만 들어가는 것을 확인할 수 있다. 이게 연관관계 없는 엔티티를 on절을 사용한 외부조인을 사용할때 사용하는 방법이다.
        List<Tuple> results = query
                .select(member, team)
                .from(member)
                .join(team)
                .on(member.username.eq(team.name)) // hibernate 5.1부터 on절을 사용해서 연관관계가 없는 엔티티를 외부 조인하는 기능이 추가되었다.(당근 내부조인도 가능함)
                .fetch();

        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }


    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    void fetchJoinNoTest() {
        em.flush();
        em.clear();

        Member result = query.selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(result.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    void fetchJoinUseTest() {
        em.flush();
        em.clear();

        Member result = query.select(member)
                .from(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(result.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    /**
     * 서브쿼리 사용하기
     */
    @Test
    void subQueryTest1() {
        // 나이가 가장 많은 회원 조회
        QMember subMember = new QMember("subMember");

        // where절에서 서브쿼리를 사용할 수 있다.
        List<Member> results = query
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(subMember.age.max())
                                .from(subMember)
                ))
                .fetch();

        for (Member result : results) {
            System.out.println("result = " + result);
        }

        assertThat(results).extracting("age")
                .containsExactly(40);
    }

    @Test
    void subQueryTest2() {
        // 나이가 평균 이상인 회원
        QMember subMember = new QMember("subMember");

        List<Member> results = query
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(subMember.age.avg())
                                .from(subMember)
                ))
                .fetch();

        assertThat(results)
                .extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    void subQueryTest3() {
        // in절을 사용한 서브쿼리
        QMember subMember = new QMember("subMember");
        List<Member> results = query
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(subMember.age)
                                .from(subMember)
                                .where(subMember.age.gt(10))
                ))
                .fetch();

        assertThat(results)
                .extracting("age")
                .containsExactly(20, 30, 40);
    }

    // 주의사항으로 서브쿼리는 From절에서는 사용할 수 없다. 이건 JPA의 한계이다. JPQL의 서브쿼리 한계점이다. 당연히 QueryDsl도 지원하지 않는다.
    @Test
    void subQueryTest4() {
        // select절의 서브쿼리
        QMember subMember = new QMember("subMember");

        List<Tuple> results = query
                .select(member.username,
                        JPAExpressions
                                .select(subMember.age.avg())
                                .from(subMember)
                )
                .from(member)
                .fetch();

        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void caseTest1() {
        List<Tuple> results = query
                .select(member.age,
                        member.age
                                .when(10).then("ten")
                                .when(20).then("twenty")
                                .otherwise("etc")
                )
                .from(member)
                .fetch();

        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void caseTest2() {

        List<Tuple> results = query.select(member.age,
                        new CaseBuilder()
                                .when(member.age.between(0, 10)).then("0~10")
                                .when(member.age.between(11, 20)).then("11~20")
                                .when(member.age.between(21, 30)).then("21~30")
                                .otherwise("etc")
                ).from(member)
                .fetch();


        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void caseTest3() {
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(21, 30)).then(1)
                .when(member.age.between(0, 20)).then(2)
                .otherwise(3);

        List<Tuple> results = query.select(member.username,
                        member.age,
                        rankPath)
                .from(member)
                .orderBy(rankPath.desc())
                .fetch();

        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void constantTest() {
        List<Tuple> results = query.select(member.age,
                        Expressions.constant("A"))
                .from(member)
//                .where(member.username.eq("member1"))
                .fetch();

        for (Tuple result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void constantTest2() {
        Tuple results = query.select(member.username,
                        member.age,
                        member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        System.out.println(results);
    }

    /**
     * 프로젝션 조회 방법
     */
    @Test
    void baseProjectionTest() {
        // 프로젝션 대상이 1개인 경우 타입을 명확하게 지정할 수 있다.
        List<Integer> result = query.select(member.age)
                .from(member)
                .fetch();
    }

    @Test
    void tupleProjectionTest() {
        // 프로젝션 대상이 2개 이상인 경우 Tuple 타입으로 조회한다.
        List<Tuple> results = query.select(member.age, member.username)
                .from(member)
                .fetch();
    }

    public static class UserDto {
        private String name;
        private int age;

//        public String getName() {
//            return name;
//        }

        public void setName(String name) {
            this.name = name;
        }

//        public int getAge() {
//            return age;
//        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "UserDto{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    void dtoBeanProjectionTest() {
        // bean으로 매핑하기 위해서는 Setter메서드를 이용해서 값을 주입한다.
        List<UserDto> results = query.select(
                        Projections.bean(UserDto.class,
                                member.username.as("name"), // 매핑할 필드이름이 다른경우 as를 사용한다.
                                member.age))
                .from(member)
                .fetch();

        for (UserDto result : results) {
            System.out.println("result = " + result);
        }
    }


    public static class UserDto2 {
        private String name;
        private int age;

        @Override
        public String toString() {
            return "UserDto2{" +
                    "name='" + name + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }

    @Test
    void dtoFieldProjectionTest() {
        // 필드는 Getter, Setter가 없어도 리플랙션을 이용해서 값을 주입한다. 따라서 필드명이 동일해야 한다.
        List<UserDto2> results = query
                .select(Projections.fields(UserDto2.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();

        for (UserDto2 result : results) {
            System.out.println("result = " + result);
        }
    }

    public static class UserDto3 {
        private String name;
        private int age;

        public UserDto3(int age, String name) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "UserDto3{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    void constructorProjectionTest() {

        // 생성자를 이용해서 매핑하는 방법. 이는 생성자 파라미터 타입순으로 매핑되기 때문에 as를 사용하지 않아도 된다.
        List<UserDto3> results = query
                .select(
                        Projections.constructor(
                                UserDto3.class,
                                member.age,
                                member.username)
                ).from(member)
                .fetch();

        for (UserDto3 result : results) {
            System.out.println("result = " + result);
        }
    }


    public static class UserDto4 {
        private String name;
        private int maxAge;

        @Override
        public String toString() {
            return "UserDto4{" +
                    "name='" + name + '\'' +
                    ", maxAge=" + maxAge +
                    '}';
        }
    }

    @Test
    void subQueryProjectionTest() {
        QMember subMember = new QMember("subMember");

        // 서브쿼리는 DTO에 매핑하기 위해서는 무조건 ExpressionUtils를 사용할 수 밖에 없다.
        List<UserDto4> results = query
                .select(
                        Projections.fields(
                                UserDto4.class,
                                member.username.as("name"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(subMember.age.max())
                                                .from(subMember), "maxAge")
                        )
                ).from(member)
                .fetch();

        for (UserDto4 result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void queryProjectionTest() {
        // 이렇게 DTO생성자에 @QueryProejection을 사용하면 Q파일이 생성된 뒤 생성자 매핑을 사용할 수 있다.
        // 장점은 컴파일 시점에 파라미터 갯수나 타입 등을 체크할 수 있다.
        // 단점은 DTO가 QueryDSL에 의존적이게 된다.
        List<UserDto5> results = query
                .select(new QUserDto5(member.username, member.age))
                .from(member)
                .fetch();

        for (UserDto5 result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void dynamicQuery_booleanBuilderTest() {
        String username = "member1";
        Integer age = 10;

        List<Member> results = searchMember(username, age);

        for (Member result : results) {
            System.out.println("result = " + result);
        }

        assertThat(results)
                .extracting("username")
                .containsExactly("member1");
    }

    private List<Member> searchMember(String username, Integer age) {
        BooleanBuilder builder = new BooleanBuilder();
        if (username != null) {
            builder.and(member.username.eq(username));
        }
        if (age != null) {
            builder.and(member.age.eq(age));
        }

        return query.select(member)
                .from(member)
                .where(builder)
                .fetch();
    }


    @Test
    void dynamicQuery_whereParam() {
        String username = "member1";
        Integer age = 10;

        List<Member> results = searchMember2(username, age);
        for (Member result : results) {
            System.out.println("result = " + result);
        }

        assertThat(results)
                .extracting("username")
                .containsExactly("member1");
    }

    private List<Member> searchMember2(String username, Integer age) {
        return query
                .select(member)
                .from(member)
                .where(
                        // 2개의 메서드를 조합해서 하나의 메서드로 만드는 것도 가능하다.
                        usernameEq(username),
                        ageEq(age)
                )
                .fetch();
    }

    //BooleanExpression 반환타입을 사용해야 and, or같이 조합해서 사용할 수 있다.
    private BooleanExpression usernameEq(String username) {
        if (Objects.isNull(username)) return null;

        return member.username.eq(username);
    }

    private BooleanExpression ageEq(Integer age) {
        if (Objects.isNull(age)) return null;

        return member.age.eq(age);
    }

    @Test
    void bulkTest() {
        List<Member> origin = query.selectFrom(member)
                .fetch();
        System.out.println("======변경 전======");
        for (Member result : origin) {
            System.out.println("result = " + result);
        }

        long count = query
                .update(member)
                .set(member.username, "비회원")
                .set(member.age, 30)
                .where(member.age.lt(28))
                .execute();
        System.out.println("update count = " + count);

        // 벌크연산은 1차캐시의 데이터는 변경하지 않고 DB에만 반영한다.
        // 이미 1차캐시에 저장되어있는 entity데이터는 select연산으로 가져와도 DB의 데이터를 변경하지 않고 1차 캐시의 데이터를 우선시 하여 조회한 데이터를 버린다. 따라서 1차 캐시의 데이터를 변경하고 싶다면 Flush후 Clear해줘야 한다.
        em.flush();
        em.clear();

        List<Member> results = query.selectFrom(member)
                .fetch();
        System.out.println("======변경 후======");
        for (Member result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void updateAddTest() {
        // 이것도 마찬가지로 1차 캐시에는 저장되지 않음. 1차 캐시로 저장하기 위해서는 Dirty Checking을 사용해야 한다.
        long count = query
                .update(member)
                .set(member.username, member.username.concat("_").concat(member.age.stringValue()))
                .where(member.age.eq(10))
                .execute();

        System.out.println("update count = " + count);

        List<Member> results = query.selectFrom(member)
                .fetch();

        for (Member result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    void dirtyCheckingTest() {
        Member findMember = query
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        // dirty checking으로 변경해야 하나의 엔티티만 변경되고 1차 캐시에도 변경된 내용을 저장하고 DB에도 저장하여 싱크가 맞게된다.
        findMember.changeUsername("testname");

        List<Member> results = query.selectFrom(member)
                .fetch();

        for (Member result : results) {
            System.out.println("result = " + result);
        }
    }


}
