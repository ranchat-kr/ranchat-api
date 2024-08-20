package com.ranchat.chatting.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.ranchat.chatting.common.ControllerTestContext;
import com.ranchat.chatting.user.service.GetUserDetailsService;
import com.ranchat.chatting.user.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.ranchat.chatting.common.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class GetUserDetailsControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.USER.tagName();
    private static final String DESCRIPTION = Tags.USER.descriptionWith("상세 조회");
    @Autowired
    private GetUserDetailsService service;


    @Test
    void success() {
        Mockito.when(service.get(any()))
            .thenReturn(new UserVo(
                UUID.randomUUID().toString(),
                "이쁜 기현이"
            ));

        given()
            .when()
            .get("/v1/users/{userId}", "uuid")
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
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("결과 데이터"),
                            fieldWithPath("data.id").type(STRING).description("유저 ID"),
                            fieldWithPath("data.name").type(STRING).description("유저 이름")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }

}