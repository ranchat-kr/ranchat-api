package com.ranchat.chatting.common.component;

import com.ranchat.chatting.common.client.DiscordClient;
import com.ranchat.chatting.common.client.FcmClient;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.exception.MessageNotFoundException;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.notification.domain.AppNotification;
import com.ranchat.chatting.notification.repository.AppNotificationRepository;
import com.ranchat.chatting.room.exception.RoomNotFoundException;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppNotificationSender {
    private final static Set<ChatMessage.SenderType> NOTIFIABLE_SENDER_TYPES = Set.of(
        ChatMessage.SenderType.USER
    );
    private final FcmClient fcmClient;
    private final AppNotificationRepository appNotificationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final DiscordClient discordClient;

    @Transactional(readOnly = true)
    public void newMessage(long messageId) {
        var message = chatMessageRepository.findById(messageId)
            .orElseThrow(MessageNotFoundException::new);

        if (!NOTIFIABLE_SENDER_TYPES.contains(message.senderType())) return;

        var room = chatRoomRepository.findById(message.roomId())
            .orElseThrow(RoomNotFoundException::new);
        var others = room.getOtherParticipants(message.participantId());

        for (var other : others) {
            var appNotifications = appNotificationRepository.findAllActiveNotifications(other.userId());

            if (appNotifications.isEmpty()) return;


            var title = message.senderType() == ChatMessage.SenderType.ADMIN
                ? "관리자로부터 새로운 메시지가 도착했습니다."
                : room.getParticipant(message.participantId()).name() + "님으로부터 새로운 메시지가 도착했습니다.";

            appNotifications.forEach(noti -> {
                var request = new FcmClient.Request(
                    noti.agentId(),
                    title,
                    message.content()
                );

                discordClient.sendMessage("알림 전송 시작: " + request);
                fcmClient.sendMessage(request);
                discordClient.sendMessage("알림 전송 성공: " + request);
            });
        }
    }
}
