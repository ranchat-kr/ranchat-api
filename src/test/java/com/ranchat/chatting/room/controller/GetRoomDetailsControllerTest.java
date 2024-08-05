package com.ranchat.chatting.room.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.GetRoomDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

class GetRoomDetailsControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.CHAT_ROOM.tagName();
    private static final String DESCRIPTION = Tags.CHAT_ROOM.descriptionWith("상세 조회");
    @Autowired
    private GetRoomDetailsService service;


    @Test
    void success() {
        Mockito.when(service.get(anyLong(), any()))
            .thenReturn(new GetRoomDetailsService.RoomDetails(
                1L,
                "방 제목",
                ChatRoom.RoomType.RANDOM,
                List.of(
                    new GetRoomDetailsService.RoomDetails.Participant(
                        1L,
                        "user-id",
                        "참가자 이름"
                    )
                )
            ));

        given()
            .param("userId", "uuid")
            .when()
            .get("/v1/rooms/{roomId}", 1L)
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
                        parameterWithName("userId").optional().description("유저 ID")
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("결과 데이터"),
                            fieldWithPath("data.id").type(NUMBER).description("방 ID"),
                            fieldWithPath("data.title").type(STRING).description("방 제목"),
                            enumDescription(
                                fieldWithPath("data.type").description("방 타입"),
                                ChatRoom.RoomType.class
                            ),
                            fieldWithPath("data.participants").type(ARRAY).description("참여자 목록"),
                            fieldWithPath("data.participants[].id").type(NUMBER).description("참여자 ID"),
                            fieldWithPath("data.participants[].userId").type(STRING).description("참여자 유저 ID"),
                            fieldWithPath("data.participants[].name").type(STRING).description("참여자 이름")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }
}