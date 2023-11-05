package com.example.springboot_querydsl.repository;

import com.example.springboot_querydsl.dto.MemberSearchCondition;
import com.example.springboot_querydsl.dto.MemberTeamDto;
import com.example.springboot_querydsl.dto.QMemberTeamDto;
import com.example.springboot_querydsl.entity.Member;
import com.example.springboot_querydsl.entity.QTeam;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.springboot_querydsl.entity.QMember.member;
import static com.example.springboot_querydsl.entity.QTeam.*;

public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(
                        Projections.fields(
                                MemberTeamDto.class,
                                member.id.as("memberId"),
                                member.username,
                                member.age,
                                member.team.id.as("teamId"),
                                member.team.name.as("teamName")
                        )
                )
                .from(member)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
                .select(
                        Projections.fields(
                                MemberTeamDto.class,
                                member.id.as("memberId"),
                                member.username,
                                member.age,
                                member.team.id.as("teamId"),
                                member.team.name.as("teamName")
                        )
                )
                .from(member)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MemberTeamDto> contents = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> contents = getContents(condition, pageable);

        long total = getCountQuery(condition);

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<MemberTeamDto> searchPageComplexOptimization(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> contents = getContents(condition, pageable);

        // PageableExecutionUtils.getPage()를 사용하면 최초 조회 시 조회갯수가 전체갯수보다 클때 등인 경우 count쿼리를 조회하지 않는다.
        // 즉 필요할때만 람다로 전달한 count쿼리를 호출한다.
        return PageableExecutionUtils.getPage(contents, pageable,
                () -> getCountQuery(condition)
        );
    }

    private List<MemberTeamDto> getContents(MemberSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.fields(
                                MemberTeamDto.class,
                                member.id.as("memberId"),
                                member.username,
                                member.age,
                                member.team.id.as("teamId"),
                                member.team.name.as("teamName")
                        )
                )
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long getCountQuery(MemberSearchCondition condition) {
        return queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetchOne();
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? member.team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

}
