package com.ranchat.chatting.report.repository;

import com.ranchat.chatting.report.domain.Report;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ChatRoomRepositoryCustom {
}
