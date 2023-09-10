package com.example.mybatis_cache.repository;

import com.example.mybatis_cache.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository {
    User findByIdNoFlush(@Param("id") int id);
    void save(@Param("user") User user);

    List<User> findAll();

    User findByIdFlush(@Param("id") int id);
}
