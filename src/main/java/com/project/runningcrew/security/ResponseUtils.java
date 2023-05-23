package com.project.runningcrew.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.exception.common.BaseErrorCode;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ResponseUtils {

    private final ObjectMapper objectMapper;

    public void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, BaseErrorCode errorCode) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .messages(errorCode.getMessage())
                .errors(Map.of())
                .build();
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

}
