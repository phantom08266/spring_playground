package com.example.springrediscache.config;

import com.example.springrediscache.domain.entity.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * User 객체만 처리할 수 있는 설정
     * @param connectionFactory
     * @return
     */
    @Bean
    RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory connectionFactory) {

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        var redisTemplate = new RedisTemplate<String, User>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, User.class));

        return redisTemplate;
    }

    /**
     * Object를 받아서 처리하는 함수(여러 Object를 받아서 처리할 수 있도록 설정 가능)
     * @param connectionFactory
     * @return
     */
    @Bean
    RedisTemplate<String, Object> objectRedisTemplate(RedisConnectionFactory connectionFactory) {

        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper()
                // json -> object 시 unknown property가 있어도 에러를 발생시키지 않음
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                // object -> json 시 변환할 클래스 타입을 같이 저장하도록 설정
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);


        var redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return redisTemplate;
    }
}
