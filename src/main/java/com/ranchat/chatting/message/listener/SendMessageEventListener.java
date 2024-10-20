package com.ranchat.chatting.message.listener;

import com.ranchat.chatting.common.component.AppNotificationSender;
import com.ranchat.chatting.common.constant.TopicType;
import com.ranchat.chatting.common.support.ExceptionUtils;
import com.ranchat.chatting.common.web.WebsocketResponse;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.message.exception.MessageNotFoundException;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.message.service.ReplyGptMessageService;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.domain.ChatRoom.RoomType;
import com.ranchat.chatting.room.exception.RoomNotFoundException;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.ranchat.chatting.common.support.ExceptionUtils.ignoreException;
import static com.ranchat.chatting.common.support.Status.MESSAGE_NOT_FOUND;
import static com.ranchat.chatting.common.support.Status.ROOM_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendMessageEventListener {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReplyGptMessageService replyGptMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AppNotificationSender appNotificationSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(Event event) {
        var message = chatMessageRepository.findById(event.roomId())
            .orElseThrow(MessageNotFoundException::new);
        var room = chatRoomRepository.findById(message.roomId())
            .orElseThrow(RoomNotFoundException::new);

        if (room.type() == RoomType.GPT) {
            var replyMessage = replyGptMessageService.reply(message.id());

            simpMessagingTemplate.convertAndSend(
                TopicType.RECEIVE_NEW_MESSAGE.endpoint().formatted(replyMessage.roomId()),
                WebsocketResponse.success(replyMessage)
            );
        }

        ignoreException(() -> appNotificationSender.newMessage(message.id()));
    }

    public record Event(
        long roomId
    ) {
    }
}
