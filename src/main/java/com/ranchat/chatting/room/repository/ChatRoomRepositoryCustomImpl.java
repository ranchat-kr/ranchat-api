package com.ranchat.chatting.room.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ranchat.chatting.common.support.JsonUtils;
import com.ranchat.chatting.room.repository.projection.ChatRoomSummaryProjection;
import com.ranchat.chatting.room.repository.projection.QChatRoomSummaryProjection;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        var roomIds = queryResult.stream()
            .map(ChatRoomSummaryProjection::id)
            .toList();

        var participantNameMap = queryFactory
            .select(chatRoom.id, chatParticipant.userId, chatParticipant.name)
            .from(chatRoom)
            .join(chatRoom.participants, chatParticipant)
            .where(
                chatParticipant.userId.ne(userId),
                chatRoom.id.in(roomIds)
            )
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(
                tuple -> tuple.get(chatRoom.id), // Room ID를 기준으로 그룹화
                Collectors.toMap(
                    tuple -> tuple.get(chatParticipant.userId), // 내부 Map의 키: userId
                    tuple -> tuple.get(chatParticipant.name)   // 내부 Map의 값: name
                )
            ));

        var randomChatRoomTitleMap = queryResult.stream()
            .collect(Collectors.toMap(
                ChatRoomSummaryProjection::id,
                room -> {
                    var list = JsonUtils.parseArray(room.title(), String.class);
                    list.removeIf(userId::equals);

                    var participantNameByUserId = Optional.ofNullable(participantNameMap.get(room.id()))
                        .orElse(Map.of());

                    var roomTitle = list.stream()
                        .filter(participantNameByUserId::containsKey)
                        .map(participantNameByUserId::get)
                        .collect(Collectors.joining(","));

                    return roomTitle.isEmpty() ? "비어있는 방" : roomTitle;
                }
            ));

        var items = queryResult.stream()
            .map(projection -> new ChatRoomSummary(
                projection.id(),
                randomChatRoomTitleMap.get(projection.id()),
                projection.type(),
                projection.latestMessage(),
                projection.latestMessageAt()
            ))
            .toList();

        return new PageImpl<>(items, pageable, total);
    }
}
