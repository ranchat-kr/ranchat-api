package com.ranchat.chatting.room.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ranchat.chatting.message.domain.QChatMessage;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.domain.QChatParticipant;
import com.ranchat.chatting.room.domain.QChatRoom;
import com.ranchat.chatting.room.service.GetRoomListService;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.stream.Stream;

import static com.ranchat.chatting.message.domain.QChatMessage.chatMessage;
import static com.ranchat.chatting.room.domain.QChatParticipant.chatParticipant;
import static com.ranchat.chatting.room.domain.QChatRoom.chatRoom;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {
    private final NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public Page<ChatRoomSummary> findAll(GetRoomListService.Request request) {
        var pageable = request.pageable();
        var where = """
            #{userIdEq}
            """
            .replace(
                "#{userIdEq}",
                request.userId()
                    .map("""
                        AND cr.id in (
                            SELECT chat_room_id
                            FROM chat_participants
                            WHERE user_id = '%s'
                        )
                        """::formatted)
                    .orElse("")
            );

        var total = jdbcTemplate.queryForObject("""
            SELECT COUNT(*)
            FROM chat_rooms cr
            WHERE 1 = 1
            %s
            """
            .formatted(where),
            new MapSqlParameterSource(),
            Long.class
        );

        if (total == null || total == 0) {
            return Page.empty();
        }

        var items = jdbcTemplate.query("""
            SELECT
                cr.id,
                cr.title,
                cr.room_type,
                cm.latest_content,
                cm.latest_created_at
            FROM chat_rooms cr
            LEFT JOIN (
               SELECT chat_room_id,
                      content as latest_content,
                      created_at as latest_created_at
                FROM chat_messages
                WHERE (chat_room_id, created_at) IN (
                    SELECT chat_room_id, MAX(created_at)
                    FROM chat_messages
                    GROUP BY chat_room_id
                )
            ) cm ON cr.id = cm.chat_room_id
            WHERE 1 = 1
            %s
            ORDER BY cm.latest_created_at DESC NULLS LAST,
                     cr.id DESC
            LIMIT :limit OFFSET :offset
            """
            .formatted(where),
            new MapSqlParameterSource()
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset()),
            (rs, rowNum) -> new ChatRoomSummary(
                rs.getLong("id"),
                rs.getString("title"),
                ChatRoom.RoomType.valueOf(rs.getString("room_type")),
                rs.getString("latest_content"),
                Optional.ofNullable(rs.getTimestamp("latest_created_at"))
                    .map(Timestamp::toLocalDateTime)
                    .orElse(null)
            )
        );

        return new PageImpl<>(
            items,
            pageable,
            total
        );
    }
}
