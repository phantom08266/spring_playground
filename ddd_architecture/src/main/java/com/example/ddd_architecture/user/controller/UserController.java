package com.example.ddd_architecture.user.controller;

import com.example.ddd_architecture.user.controller.dto.MyProfileResponse;
import com.example.ddd_architecture.user.controller.dto.UserResponse;
import com.example.ddd_architecture.user.controller.dto.UserUpdateDto;
import com.example.ddd_architecture.user.infra.UserEntity;
import com.example.ddd_architecture.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(toResponse(userService.getByIdOrElseThrow(id)));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
        @PathVariable long id,
        @RequestParam String certificationCode) {
        userService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponse> getMyInfo(
        @RequestHeader("EMAIL") String email // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
    ) {
        UserEntity userEntity = userService.getByEmail(email);
        userService.login(userEntity.getId());
        return ResponseEntity
            .ok()
            .body(toMyProfileResponse(userEntity));
    }

    @PutMapping("/me")
    public ResponseEntity<MyProfileResponse> updateMyInfo(
        @RequestHeader("EMAIL") String email, // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
        @RequestBody UserUpdateDto userUpdateDto
    ) {
        UserEntity userEntity = userService.getByEmail(email);
        userEntity = userService.updateUser(userEntity.getId(), userUpdateDto);
        return ResponseEntity
            .ok()
            .body(toMyProfileResponse(userEntity));
    }

    public UserResponse toResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setNickname(userEntity.getNickname());
        userResponse.setStatus(userEntity.getStatus());
        userResponse.setLastLoginAt(userEntity.getLastLoginAt());
        return userResponse;
    }

    public MyProfileResponse toMyProfileResponse(UserEntity userEntity) {
        MyProfileResponse myProfileResponse = new MyProfileResponse();
        myProfileResponse.setId(userEntity.getId());
        myProfileResponse.setEmail(userEntity.getEmail());
        myProfileResponse.setNickname(userEntity.getNickname());
        myProfileResponse.setStatus(userEntity.getStatus());
        myProfileResponse.setAddress(userEntity.getAddress());
        myProfileResponse.setLastLoginAt(userEntity.getLastLoginAt());
        return myProfileResponse;
    }
}