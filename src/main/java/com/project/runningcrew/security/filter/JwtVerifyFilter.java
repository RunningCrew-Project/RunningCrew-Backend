package com.project.runningcrew.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.security.CustomUserDetail;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JwtVerifyFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final String SECRET_KEY;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtVerifyFilter(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           String SECRET_KEY) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.SECRET_KEY = SECRET_KEY;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtHeader.replace("Bearer ", "");
        String email = null;
        try {
            email =  Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .messages("토큰 값이 만료되었습니다.")
                    .errors(Map.of())
                    .build();
            objectMapper.writeValue(response.getOutputStream(), errorResponse);
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpServletResponse.SC_FORBIDDEN)
                    .messages("토큰 형식이 올바르지 않습니다.")
                    .errors(Map.of())
                    .build();
            objectMapper.writeValue(response.getOutputStream(), errorResponse);
            return;
        }

        if (email != null) {
            User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
            UserRole userRole = userRoleRepository.findByUser(user).orElseThrow(UserRoleNotFoundException::new);
            CustomUserDetail customUserDetail = new CustomUserDetail(user, userRole);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetail, null, customUserDetail.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}
