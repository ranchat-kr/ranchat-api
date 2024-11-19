package com.ranchat.chatting.room.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.GetJoinedRoomListService;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

class GetJoinedRoomListControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.CHAT_ROOM.tagName();
    private static final String DESCRIPTION = Tags.CHAT_ROOM.descriptionWith("목록 조회");
    @Autowired
    private GetJoinedRoomListService service;


    @Test
    void success() {
        Mockito.when(service.get(any()))
            .thenReturn(mockResult());

        given()
            .param("page", 1)
            .param("size", 20)
            .param("userId", UUID.randomUUID().toString())
            .when()
            .get("/v1/rooms")
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
                    queryParameters(
                        parameterWithName("page").description("페이지 번호"),
                        parameterWithName("size").description("페이지 사이즈"),
                        parameterWithName("userId").description("유저 ID")
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("결과 데이터"),
                            fieldWithPath("data.items").type(ARRAY).description("결과 데이터"),
                            fieldWithPath("data.items[].id").type(NUMBER).description("방 ID"),
                            fieldWithPath("data.items[].title").type(STRING).description("방 제목"),
                            enumDescription(
                                fieldWithPath("data.items[].type").description("방 타입"),
                                ChatRoom.RoomType.class
                            ),
                            fieldWithPath("data.items[].latestMessage").type(STRING).description("최근 메시지"),
                            fieldWithPath("data.items[].latestMessageAt").type(STRING).description("최근 메시지 시간"),
                            fieldWithPath("data.page").type(NUMBER).description("페이지 번호"),
                            fieldWithPath("data.size").type(NUMBER).description("페이지 사이즈"),
                            fieldWithPath("data.totalCount").type(NUMBER).description("전체 개수"),
                            fieldWithPath("data.totalPage").type(NUMBER).description("전체 페이지 수"),
                            fieldWithPath("data.empty").type(BOOLEAN).description("빈 리스트 여부")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }

    private Page<ChatRoomSummary> mockResult() {
        return new PageImpl<>(
            List.of(
                new ChatRoomSummary(
                    1L,
                    "room-title",
                    ChatRoom.RoomType.RANDOM,
                    "latest-content",
                    LocalDateTime.now()
                )
            ),
            PageRequest.of(0, 20),
            1
        );
    }
}