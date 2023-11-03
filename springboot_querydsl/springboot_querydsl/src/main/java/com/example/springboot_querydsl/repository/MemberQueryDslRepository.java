package com.example.springboot_querydsl.repository;

import com.example.springboot_querydsl.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.springboot_querydsl.entity.QMember.member;

@Repository
public class MemberQueryDslRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;


    public MemberQueryDslRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Member member) {
        em.persist(member);
    }

    public List<Member> findAll() {
        return query
                .selectFrom(member)
                .fetch();
    }

    public Member findById(Long id) {
        return query
                .selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    public List<Member> findByUsername(String username) {
        return query
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }
}
