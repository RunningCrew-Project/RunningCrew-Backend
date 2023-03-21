package com.project.runningcrew.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;
import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.refreshtoken.repository.RefreshTokenRepository;
import com.project.runningcrew.security.CustomUserDetail;
import com.project.runningcrew.security.JwtProvider;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
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
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST") || !request.getContentType().equals("application/json")) {
            throw new AuthenticationServiceException("잘못된 형식의 요청입니다.");
        }

        String fcmToken = request.getHeader("FcmToken");
        if (fcmToken == null) {
            throw new AuthenticationServiceException("FcmToken 이 누락되었습니다.");
        }

        try {
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
        User user = customUserDetail.getUser();
        UserRole userRole = customUserDetail.getUserRole();

        try {
            String fcmToken = request.getHeader("FcmToken");
            if (fcmTokenRepository.existsByUser(user)) {
                throw new RuntimeException("이미 로그인된 유저입니다.");
            }
            fcmTokenRepository.save(new FcmToken(user, fcmToken));

            String accessToken = jwtProvider.createAccessToken(user, userRole);
            if (refreshTokenRepository.findByUser(user).isPresent()) {
                throw new RuntimeException("이미 로그인된 유저입니다.");
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
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .messages(e.getMessage())
                    .errors(Map.of())
                    .build();
            objectMapper.writeValue(response.getOutputStream(), errorResponse);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .messages(failed.getMessage())
                .errors(Map.of())
                .build();
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    @Getter
    private static class LoginDto {
        private String email;
        private String password;
    }

}
