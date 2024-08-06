package com.ranchat.chatting.room.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.room.service.CheckExistRoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

class CheckExistRoomControllerTest extends ControllerTestContext {
    private static final String TAG = ControllerTestContext.Tags.CHAT_ROOM.tagName();
    private static final String DESCRIPTION = ControllerTestContext.Tags.CHAT_ROOM.descriptionWith("유저 ID로 존재 여부 확인");
    @Autowired
    private CheckExistRoomService service;


    @Test
    void success() {
        Mockito.when(service.check(any()))
            .thenReturn(true);

        given()
            .param("userId", "uuid")
            .when()
            .get("/v1/rooms/exists-by-userId")
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
                            fieldWithPath("data").type(BOOLEAN).description("결과 데이터")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }

}