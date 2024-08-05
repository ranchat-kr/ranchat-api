package com.ranchat.chatting.report.component;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.report.domain.Report;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportValidatorImpl implements ReportValidator {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public void validateCreation(Report report) {
        var room = chatRoomRepository.findById(report.roomId())
            .orElseThrow(() -> new BadRequestException(Status.ROOM_NOT_FOUND));

        checkParticipant(room, report.reporterId());
        checkParticipant(room, report.reportedUserId());
    }

    private void checkParticipant(ChatRoom room, String userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new BadRequestException(Status.USER_NOT_FOUND));

        if (!room.isParticipant(user.id())) {
            throw new BadRequestException(
                Status.INVALID_PARAMETER,
                "해당 방의 참여자가 아니라 신고가 불가합니다. userId: %s".formatted(user.id())
            );
        }
    }
}
