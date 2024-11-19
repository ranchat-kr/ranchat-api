package com.ranchat.chatting.common.component;

import com.ranchat.chatting.common.client.FcmClient;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.exception.MessageNotFoundException;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.notification.domain.AppNotification;
import com.ranchat.chatting.notification.domain.AppNotificationHistory;
import com.ranchat.chatting.notification.domain.AppNotificationHistory.Status;
import com.ranchat.chatting.notification.domain.AppNotificationHistory.Type;
import com.ranchat.chatting.notification.repository.AppNotificationHistoryRepository;
import com.ranchat.chatting.notification.repository.AppNotificationRepository;
import com.ranchat.chatting.room.exception.RoomNotFoundException;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppNotificationSender {
    private final static Set<ChatMessage.SenderType> NOTIFIABLE_SENDER_TYPES = Set.of(
        ChatMessage.SenderType.USER
    );
    private final FcmClient fcmClient;
    private final AppNotificationRepository appNotificationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AppNotificationHistoryRepository appNotificationHistoryRepository;

    @Transactional
    public void newMessage(long messageId) {
        var message = chatMessageRepository.findById(messageId)
            .orElseThrow(MessageNotFoundException::new);

        if (!NOTIFIABLE_SENDER_TYPES.contains(message.senderType())) return;

        var room = chatRoomRepository.findById(message.roomId())
            .orElseThrow(RoomNotFoundException::new);
        var others = room.getOtherParticipants(message.participantId());

        for (var other : others) {
            var appNotifications = appNotificationRepository.findAllActiveNotifications(other.userId())
                .stream()
                .filter(AppNotification::allowsNotification)
                .toList();

            if (appNotifications.isEmpty()) return;

            var title = message.senderType() == ChatMessage.SenderType.ADMIN
                ? "관리자 메시지"
                : room.getParticipant(message.participantId()).name() + "님의 메시지";

            appNotifications.forEach(noti -> {
                AppNotificationHistory history = null;
                var request = new FcmClient.Request(
                    noti.agentId(),
                    title,
                    message.content()
                );

                try {
                    fcmClient.sendMessage(request);
                    history = createSuccessHistory(noti, Type.SEND_MESSAGE, request);
                } catch (Exception e) {
                    log.error("fail to send fcm message", e);
                    history = createFailureHistory(noti, Type.SEND_MESSAGE, e, request);
                } finally {
                    appNotificationHistoryRepository.save(history);
                }
            });
        }
    }

    private AppNotificationHistory createSuccessHistory(AppNotification appNotification,
                                                        Type historyType,
                                                        FcmClient.Request request) {
        return createHistory(appNotification, historyType, Status.SUCCESS, null, request);
    }

    private AppNotificationHistory createFailureHistory(AppNotification appNotification,
                                                        Type historyType,
                                                        Exception e,
                                                        FcmClient.Request request) {
        return createHistory(appNotification, historyType, Status.FAILURE, e.getMessage(), request);
    }

    private AppNotificationHistory createHistory(AppNotification appNotification,
                                                 Type historyType,
                                                 Status historyStatus,
                                                 String failReason,
                                                 FcmClient.Request request) {
        return new AppNotificationHistory(
            historyType,
            historyStatus,
            appNotification.userId(),
            appNotification.agentId(),
            appNotification.id(),
            request.title(),
            request.content(),
            failReason
        );
    }
}
