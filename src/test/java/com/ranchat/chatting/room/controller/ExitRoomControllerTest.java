package com.ranchat.chatting.room.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.report.controller.CreateReportController;
import com.ranchat.chatting.report.domain.Report;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

class ExitRoomControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.CHAT_ROOM.tagName();
    private static final String DESCRIPTION = Tags.CHAT_ROOM.descriptionWith("퇴장");

    @Test
    void success() {
        var requestBody = new ExitRoomController.Request("user-id");

        given()
            .body(requestBody)
            .when()
            .post("/v1/rooms/{roomId}/exit", 1L)
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
                        fieldWithPath("userId").type(STRING).description("사용자 ID")
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }
}