package com.project.runningcrew.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;
import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.refreshtoken.repository.RefreshTokenRepository;
import com.project.runningcrew.security.CustomUserDetail;
import com.project.runningcrew.security.JwtProvider;
import com.project.runningcrew.security.ResponseUtils;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FcmTokenRepository fcmTokenRepository;
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
        if (fcmTokenRepository.existsByUser(user)) {
            responseUtils.setErrorResponse(response, HttpStatus.CONFLICT, LoginErrorCode.ALREADY_LOGIN);
        }
        fcmTokenRepository.save(new FcmToken(user, fcmToken));

        String accessToken = jwtProvider.createAccessToken(user, userRole);
        if (refreshTokenRepository.findByUser(user).isPresent()) {
            responseUtils.setErrorResponse(response, HttpStatus.CONFLICT, LoginErrorCode.ALREADY_LOGIN);
        }
        String refreshToken = jwtProvider.createRefreshToken(user);
        refreshTokenRepository.save(new RefreshToken(user, refreshToken));


        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        response.setHeader("Authorization", "Bearer " + accessToken);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60 * 24 * 14);
        cookie.setPath("/");
        response.addCookie(cookie);

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
