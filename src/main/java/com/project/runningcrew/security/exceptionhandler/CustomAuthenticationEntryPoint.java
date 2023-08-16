package com.project.runningcrew.security.exceptionhandler;

import com.project.runningcrew.exception.auth.AuthErrorCode;
import com.project.runningcrew.security.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseUtils responseUtils;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        responseUtils.setErrorResponse(response, HttpStatus.UNAUTHORIZED, AuthErrorCode.AUTHENTICATION);
    }

}
