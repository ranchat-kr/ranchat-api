package com.ranchat.chatting.room.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.room.domain.ChatRoom;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class CreateRoomControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.USER.tagName();
    private static final String DESCRIPTION = Tags.USER.descriptionWith("생성");

    @Test
    void success() {
        given()
            .body(requestBody())
            .when()
            .post("/v1/rooms")
            .then()
            .log().all()
            .apply(
                document(
                    identifier(),
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .description(DESCRIPTION),
                    preprocessRequest(),
                    preprocessResponse(),
                    requestFields(
                        fieldWithPath("userIds").type(ARRAY).description("유저 아이디 목록"),
                        fieldWithPath("title").type(STRING).description("방 제목").optional(),
                        enumDescription(
                            fieldWithPath("roomType").type(STRING).description("방 타입"),
                            ChatRoom.RoomType.class
                        )
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(NUMBER).description("응답 데이터")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }

    private CreateRoomController.Request requestBody() {
        var uuid = UUID.randomUUID().toString();

        return new CreateRoomController.Request(
            List.of(uuid),
            Optional.of("test-room"),
            ChatRoom.RoomType.GPT
        );
    }
}