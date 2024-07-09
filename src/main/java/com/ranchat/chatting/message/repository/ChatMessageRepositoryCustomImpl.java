package com.ranchat.chatting.message.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.domain.QChatMessage;
import com.ranchat.chatting.message.service.GetMessageListService;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.domain.QChatParticipant;
import com.ranchat.chatting.room.domain.QChatRoom;
import com.ranchat.chatting.user.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.ranchat.chatting.message.domain.QChatMessage.chatMessage;
import static com.ranchat.chatting.room.domain.QChatParticipant.chatParticipant;
import static com.ranchat.chatting.room.domain.QChatRoom.chatRoom;
import static com.ranchat.chatting.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatMessageVo> findChatMessageList(GetMessageListService.Requirement requirement) {
        var where = Stream.of(
                Optional.of(
                    chatMessage.roomId.eq(requirement.roomId())
                ),
                requirement.createdAt()
                    .map(chatMessage.createdAt::after)
            )
            .flatMap(Optional::stream)
            .toArray(BooleanExpression[]::new);

        var total = queryFactory
            .select(Wildcard.count)
            .from(chatMessage)
            .where(where)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty();
        }

        var items = queryFactory
            .select(Projections.constructor(
                ChatMessageVo.class,
                chatMessage.id,
                chatMessage.roomId,
                chatParticipant.userId,
                chatMessage.participantId,
                chatParticipant.name,
                chatMessage.content,
                chatMessage.messageType,
                chatMessage.contentType,
                chatMessage.senderType,
                chatMessage.createdAt
            ))
            .from(chatMessage)
            .join(chatParticipant).on(chatParticipant.id.eq(chatMessage.participantId))
            .where(where)
            .orderBy(chatMessage.createdAt.desc())
            .offset(requirement.pageable().getOffset())
            .limit(requirement.pageable().getPageSize())
            .fetch();

        return new PageImpl<>(
            items,
            requirement.pageable(),
            total
        );
    }
}
