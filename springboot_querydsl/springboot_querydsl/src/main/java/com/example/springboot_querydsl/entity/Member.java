package com.example.springboot_querydsl.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(username, 0);
    }
    public Member(String username, int age) {
        this(username, age, null);
    }
    public Member(String username, int age, Team newTeam) {
        this.username = username;
        this.age = age;
        if (newTeam != null) {
            changeTeam(newTeam);
        }
    }

    public void changeTeam(Team newTeam) {
        this.team = newTeam;
        newTeam.getMembers().add(this);
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }
}
