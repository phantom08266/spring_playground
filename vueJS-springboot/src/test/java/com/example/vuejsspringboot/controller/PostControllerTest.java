package com.example.vuejsspringboot.controller;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.vuejsspringboot.controller.dto.CreatePost;
import com.example.vuejsspringboot.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    MockMvc mock;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void clear() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글이 정상적으로 등록한다.")
    void test1() throws Exception {
        // given
        String title= "test";
        String content = "test content";
        CreatePost createPost = new CreatePost(title, content);

        mock.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createPost)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글이 제목이 없는 경우 에러가 발생한다.")
    void test2() throws Exception {
        String title= null;
        String content = "test content";
        CreatePost createPost = new CreatePost(title, content);

        mock.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createPost)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("요청값이 올바르지 않습니다"))
                .andExpect(jsonPath("$.detail.title").value("타이틀은 필수값 입니다"));
    }

    @Test
    @DisplayName("게시글 등록 시 컨텐츠가 없는 경우 에러가 발생한다.")
    void test3() throws Exception {
        String title= "test";
        String content = null;
        CreatePost createPost = new CreatePost(title, content);

        mock.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createPost)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("요청값이 올바르지 않습니다"))
                .andExpect(jsonPath("$.detail.content").value("컨텐츠는 필수값 입니다"));
    }

    @Test
    @DisplayName("게시글이 디비에 저장된다.")
    void test4() throws Exception {
        String title= "test";
        String content = "test content";
        CreatePost createPost = new CreatePost(title, content);

        mock.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createPost)))
                .andDo(print())
                .andExpect(status().isOk());

        long postCount = postRepository.count();
        assertThat(postCount).isEqualTo(1);
    }
}