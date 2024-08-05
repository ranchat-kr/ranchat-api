package com.ranchat.chatting.report.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.report.domain.Report;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

class CreateReportControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.REPORT.tagName();
    private static final String DESCRIPTION = Tags.REPORT.descriptionWith("생성");

    @Test
    void success() {
        var body = new CreateReportController.Request(
            1L,
            "reporterId",
            "reportedUserId",
            Report.ReportType.SPAM,
            "신고 사유"
        );

        given()
            .body(body)
            .when()
            .post("/v1/reports")
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
                        fieldWithPath("roomId").type(NUMBER).description("방 ID"),
                        fieldWithPath("reporterId").type(STRING).description("신고자 ID"),
                        fieldWithPath("reportedUserId").type(STRING).description("신고 대상 ID"),
                        enumDescription(
                            fieldWithPath("reportType").type(STRING).description("신고 타입"),
                            Report.ReportType.class
                        ),
                        fieldWithPath("reportReason").type(STRING).description("신고 사유")
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