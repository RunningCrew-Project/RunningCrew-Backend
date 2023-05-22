package com.project.runningcrew.security.exceptionhandler;

import com.project.runningcrew.exception.auth.AuthErrorCode;
import com.project.runningcrew.security.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ResponseUtils responseUtils;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        responseUtils.setErrorResponse(response, HttpStatus.FORBIDDEN, AuthErrorCode.AUTHORIZATION);
    }

}
