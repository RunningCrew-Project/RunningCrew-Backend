package com.project.runningcrew.security.filter;

import com.project.runningcrew.exception.jwt.JwtErrorCode;
import com.project.runningcrew.exception.notFound.NotFoundErrorCode;
import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.security.CustomUserDetail;
import com.project.runningcrew.security.ResponseUtils;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtVerifyFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final String SECRET_KEY;
    private ResponseUtils responseUtils;


    public JwtVerifyFilter(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           String SECRET_KEY,
                           ResponseUtils responseUtils) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.SECRET_KEY = SECRET_KEY;
        this.responseUtils = responseUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtHeader.replace("Bearer ", "");
        String email;
        try {
            email = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            responseUtils.setErrorResponse(response, HttpStatus.UNAUTHORIZED, JwtErrorCode.JWT_EXPIRED);
            return;
        } catch (Exception e) {
            responseUtils.setErrorResponse(response, HttpStatus.BAD_REQUEST, JwtErrorCode.JWT_INVALID);
            return;
        }

        if (email != null) {
            try {
                User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
                UserRole userRole = userRoleRepository.findByUser(user).orElseThrow(UserRoleNotFoundException::new);
                CustomUserDetail customUserDetail = new CustomUserDetail(user, userRole);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        customUserDetail, null, customUserDetail.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("user", email);
            } catch (UserNotFoundException e) {
                responseUtils.setErrorResponse(response, HttpStatus.NOT_FOUND, NotFoundErrorCode.USER_NOT_FOUND);
                return;
            } catch (UserRoleNotFoundException e) {
                responseUtils.setErrorResponse(response, HttpStatus.NOT_FOUND, NotFoundErrorCode.USER_ROLE_NOT_FOUND);
                return;
            }
        }
        chain.doFilter(request, response);
    }

}
