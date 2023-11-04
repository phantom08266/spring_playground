package com.example.springboot_querydsl.repository;

import com.example.springboot_querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSpringDataJpaRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    List<Member> findByUsername(String username);
}
