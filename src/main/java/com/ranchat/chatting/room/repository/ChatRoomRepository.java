package com.ranchat.chatting.room.repository;

import com.ranchat.chatting.room.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {

    @Query("""
    SELECT
        CASE WHEN COUNT(c) > 0
        THEN TRUE
        ELSE FALSE END
    FROM ChatRoom c
    JOIN c.participants p
    WHERE p.userId = :userId
    """)
    boolean existsByUserId(String userId);

}
