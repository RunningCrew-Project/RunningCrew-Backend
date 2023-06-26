package com.project.runningcrew.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.security.CustomUserDetail;
import com.project.runningcrew.security.JwtProvider;
import com.project.runningcrew.security.ResponseUtils;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ResponseUtils responseUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST") || !request.getContentType().equals("application/json")) {
            try {
                responseUtils.setErrorResponse(response, HttpStatus.BAD_REQUEST, LoginErrorCode.BAD_REQUEST);
            } catch (IOException e) {
                log.error("ErrorResponse mapping exception : {}", e);
            }
            return null;
        }

        String fcmToken = request.getHeader("FcmToken");
        if (fcmToken == null) {
            try {
                responseUtils.setErrorResponse(response, HttpStatus.BAD_REQUEST, LoginErrorCode.FCM_TOKEN);
            } catch (IOException e) {
                log.error("ErrorResponse mapping exception : {}", e);
            }
            return null;
        }

        try {
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            log.error("loginDTo mapping exception : {}", e);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
        User user = customUserDetail.getUser();
        UserRole userRole = customUserDetail.getUserRole();

        String fcmToken = request.getHeader("FcmToken");
        String accessToken = jwtProvider.createAccessToken(user, userRole);
        String refreshToken = jwtProvider.createRefreshToken(user);

        responseUtils.setUserTokens(user, fcmToken, refreshToken);
        responseUtils.setSuccessResponse(response, HttpStatus.OK,
                Map.of("accessToken", accessToken,
                        "refreshToken", refreshToken));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        responseUtils.setErrorResponse(response, HttpStatus.UNAUTHORIZED, LoginErrorCode.INVALID);

    }

    @Getter
    private static class LoginDto {
        private String email;
        private String password;
    }


}
