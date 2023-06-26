package com.project.runningcrew.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.exception.common.BaseErrorCode;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;
import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.refreshtoken.repository.RefreshTokenRepository;
import com.project.runningcrew.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResponseUtils {

    private final FcmTokenRepository fcmTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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

    public void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Map<String, String> successBody) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        objectMapper.writeValue(response.getOutputStream(), successBody);
    }

    @Transactional
    public void setUserTokens(User user, String fcmToken, String refreshToken) {
        fcmTokenRepository.findByUser(user)
                .ifPresentOrElse(
                        existingToken -> existingToken.updateFcmToken(fcmToken),
                        () -> fcmTokenRepository.save(new FcmToken(user, fcmToken))
                );

        refreshTokenRepository.findByUser(user)
                .ifPresentOrElse(
                        existingToken -> existingToken.updateRefreshToken(refreshToken),
                        () -> refreshTokenRepository.save(new RefreshToken(user, refreshToken))
                );
    }

}
