package com.ranchat.chatting.notification.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.notification.domain.AppNotification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class UpdateAppNotificationControllerTest extends ControllerTestContext {
    private static final String TAG = ControllerTestContext.Tags.APP_NOTIFICATION.tagName();
    private static final String DESCRIPTION = ControllerTestContext.Tags.APP_NOTIFICATION.descriptionWith("수정");

    @Test
    void success() {
        var requestBody = new UpdateAppNotificationController.Request(
            "userId",
            "agentId",
            true
        );

        given()
            .body(requestBody)
            .when()
            .put("/v1/app-notifications")
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
                        fieldWithPath("userId").type(STRING).description("유저 ID"),
                        fieldWithPath("agentId").type(STRING).description("에이전트 ID"),
                        fieldWithPath("allowsNotification").type(BOOLEAN).description("알림 허용 여부")
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