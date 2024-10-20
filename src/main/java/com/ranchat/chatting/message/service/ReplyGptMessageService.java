package com.ranchat.chatting.message.service;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.exception.MessageNotFoundException;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.domain.ChatRoom.RoomType;
import com.ranchat.chatting.room.exception.RoomNotFoundException;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ranchat.chatting.common.support.Status.*;
import static com.ranchat.chatting.user.domain.User.GPT_ID;

@Service
@RequiredArgsConstructor
public class ReplyGptMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatClient chatClient;

    @Transactional
    public ChatMessageVo reply(long messageId) {
        var message = chatMessageRepository.findById(messageId)
            .orElseThrow(MessageNotFoundException::new);
        var room = chatRoomRepository.findById(message.roomId())
            .orElseThrow(RoomNotFoundException::new);

        if (room.type() != RoomType.GPT) {
            throw new BadRequestException(NOT_ALLOW_TO_SEND_GPT_MESSAGE_BY_ROOM_TYPE);
        }

        var replyContent = chatClient.prompt()
            .user(message.content())
            .call()
            .content();
        var replyMessage = new ChatMessage(
            message.roomId(),
            room.getParticipant(GPT_ID).id(),
            replyContent,
            ChatMessage.MessageType.CHAT,
            ChatMessage.ContentType.TEXT,
            ChatMessage.SenderType.USER
        );

        var savedMessage = chatMessageRepository.save(replyMessage);

        return ChatMessageVo.of(
            savedMessage,
            room.getParticipant(GPT_ID)
        );
    }

}
