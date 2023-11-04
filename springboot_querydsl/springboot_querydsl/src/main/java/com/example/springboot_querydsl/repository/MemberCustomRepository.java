package com.example.springboot_querydsl.repository;

import com.example.springboot_querydsl.dto.MemberSearchCondition;
import com.example.springboot_querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberCustomRepository {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
