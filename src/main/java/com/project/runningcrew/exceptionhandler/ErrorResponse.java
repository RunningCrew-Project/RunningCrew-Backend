package com.project.runningcrew.exceptionhandler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {

    @Schema(description = "HTTP status")
    private int status;

    @Schema(description = "에러 메시지")
    private String messages;

    @Schema(description = "에러 세부 내용")
    private Map<String, String> errors;

    @Builder
    public ErrorResponse(int status, String messages, Map<String, String> errors) {
        this.status = status;
        this.messages = messages;
        this.errors = errors;
    }

}
