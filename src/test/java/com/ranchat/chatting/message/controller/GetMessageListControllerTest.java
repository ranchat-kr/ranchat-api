package com.ranchat.chatting.message.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.service.GetMessageListService;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.GetRoomDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

class GetMessageListControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.CHAT_MESSAGE.tagName();
    private static final String DESCRIPTION = Tags.CHAT_MESSAGE.descriptionWith("목록 조회");
    @Autowired
    private GetMessageListService service;


    @Test
    void success() {
        Mockito.when(service.get(any()))
            .thenReturn(mockResult());

        given()
            .param("page", 1)
            .param("size", 10)
            .param("createdAt", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
            .when()
            .get("/v1/rooms/{roomId}/messages", "1")
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
                        parameterWithName("createdAt").optional().description("생성 시간 필터 EX) 2021-08-01T00:00:00")
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("결과 데이터"),
                            fieldWithPath("data.items").type(ARRAY).description("결과 데이터"),
                            fieldWithPath("data.items[].id").type(NUMBER).description("메시지 ID"),
                            fieldWithPath("data.items[].roomId").type(NUMBER).description("방 ID"),
                            fieldWithPath("data.items[].userId").type(STRING).description("유저 ID"),
                            fieldWithPath("data.items[].participantId").type(NUMBER).description("참여자 ID"),
                            fieldWithPath("data.items[].participantName").type(STRING).description("참여자 이름"),
                            fieldWithPath("data.items[].content").type(STRING).description("메시지 내용"),
                            enumDescription(
                                fieldWithPath("data.items[].messageType").description("메시지 타입"),
                                ChatMessage.MessageType.class
                            ),
                            enumDescription(
                                fieldWithPath("data.items[].contentType").description("컨텐츠 타입"),
                                ChatMessage.ContentType.class
                            ),
                            enumDescription(
                                fieldWithPath("data.items[].senderType").description("보낸 사람 타입"),
                                ChatMessage.SenderType.class
                            ),
                            fieldWithPath("data.items[].createdAt").type(STRING).description("생성 시간"),
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

    private Page<ChatMessageVo> mockResult() {
        return new PageImpl<>(
            List.of(
                new ChatMessageVo(
                    1L,
                    1L,
                    "user-id",
                    1L,
                    "참여자 이름",
                    "메시지",
                    ChatMessage.MessageType.CHAT,
                    ChatMessage.ContentType.TEXT,
                    ChatMessage.SenderType.USER,
                    LocalDateTime.now()
                )
            ),
            PageRequest.of(0, 10),
            1
        );
    }
}