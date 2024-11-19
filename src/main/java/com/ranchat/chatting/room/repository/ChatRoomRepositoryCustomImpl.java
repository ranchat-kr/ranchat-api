package com.ranchat.chatting.room.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ranchat.chatting.room.repository.projection.QChatRoomSummaryProjection;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.ranchat.chatting.message.domain.QChatMessage.chatMessage;
import static com.ranchat.chatting.room.domain.QChatParticipant.chatParticipant;
import static com.ranchat.chatting.room.domain.QChatRoom.chatRoom;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoomSummary> findJoinedChatRooms(Pageable pageable, String userId) {
        /*
        TIP) QueryDSL -> Native Query
        SELECT *
        FROM chat_messages
        WHERE (chat_room_id, created_at) IN (SELECT chat_room_id, max(created_at)
                                             FROM chat_messages
                                             WHERE chat_room_id in (SELECT cr.id
                                                                    FROM chat_rooms cr
                                                                    JOIN chat_participants cp ON cp.chat_room_id = cr.id
                                                                    WHERE cp.user_id = :userId)
                                             GROUP BY chat_room_id);
         */
        var baseQuery = queryFactory
            .from(chatRoom)
            .leftJoin(chatMessage).on(chatRoom.id.eq(chatMessage.roomId))
            .where(Expressions.list(chatMessage.roomId, chatMessage.createdAt).in(
                JPAExpressions.select(chatMessage.roomId, chatMessage.createdAt.max())
                    .from(chatMessage)
                    .where(chatMessage.roomId.in(
                        JPAExpressions.select(chatRoom.id)
                            .from(chatRoom)
                            .join(chatRoom.participants, chatParticipant)
                            .where(chatParticipant.userId.eq(userId))
                    ))
                    .groupBy(chatMessage.roomId)
            ));

        var total = baseQuery
            .select(Wildcard.count)
            .fetchOne();

        if (total == null || total == 0) {
            return Page.empty();
        }

        var queryResult = baseQuery
            .select(new QChatRoomSummaryProjection(
                chatRoom.id,
                chatRoom.title,
                chatRoom.type,
                chatMessage.content,
                chatMessage.createdAt
            ))
            .orderBy(
                chatMessage.createdAt.desc().nullsLast(),
                chatRoom.id.desc()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

            // TODO: 채팅방 이름 로직 구현
//        var participantIds = queryResult.stream()
//            .map(ChatRoomSummaryProjection::title)
//            .map(title -> JsonUtils.parseArray(title, Long.class))
//            .flatMap(List::stream)
//            .toList();
//
//        var participantNameMap = queryFactory
//            .select(chatParticipant.id, chatParticipant.name)
//            .from(chatParticipant)
//            .where(chatParticipant.id.in(participantIds))
//            .fetch()
//            .stream()
//            .collect(Collectors.toMap(
//                tuple -> tuple.get(chatParticipant.id),
//                tuple -> tuple.get(chatParticipant.name)
//            ));
//
//        var randomChatRoomTitleMap = queryResult.stream()
//            .map(ChatRoomSummaryProjection::title)
//            .map(title -> JsonUtils.parseArray(title, Long.class))
//            .map(ids -> {
//                ids.removeIf(participantId -> ;
//
//                return null;
//            })

        var items = queryResult.stream()
            .map(projection -> new ChatRoomSummary(
                projection.id(),
                projection.title(),
                projection.type(),
                projection.latestMessage(),
                projection.latestMessageAt()
            ))
            .toList();

        return new PageImpl<>(items, pageable, total);
    }
}
