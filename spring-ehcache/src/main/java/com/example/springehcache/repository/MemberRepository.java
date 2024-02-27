package com.example.springehcache.repository;

import com.example.springehcache.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m")
    List<Member> findAllByMemberWithPagination(Pageable pageable);
}
