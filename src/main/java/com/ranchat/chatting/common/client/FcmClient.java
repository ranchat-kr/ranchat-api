package com.ranchat.chatting.common.client;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class FcmClient {

    public void sendMessage(Request request) {
        var message = createMessage(request);

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("fail to send fcm message", e);
            throw new ServerException(Status.FAIL_TO_SEND_FCM_MESSAGE);
        }
    }

    public Message createMessage(Request request) {
        var notification = Notification
            .builder()
            .setTitle(request.title())
            .setBody(request.content())
            .build();

        return Message
            .builder()
            .setNotification(notification)
            .setToken(request.token())
            .build();
    }

    public record Request(
        String token,
        String title,
        String content
    ) {
    }
}
