package com.ranchat.chatting.user.domain;


import com.ranchat.chatting.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Table(name = "users")
@Entity
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    public static final String GPT_ID = "GPT";

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    public User(String id,
                String name) {
        this.id = id;
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }
}
