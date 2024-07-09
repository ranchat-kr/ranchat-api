package com.ranchat.chatting.user.repository;

import com.ranchat.chatting.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
