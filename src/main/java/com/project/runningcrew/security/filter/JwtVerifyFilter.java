package com.project.runningcrew.security.filter;

import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.security.CustomUserDetail;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.access.AccessDeniedException;
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

public class JwtVerifyFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final String SECRET_KEY;

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
        String email = getEmail(jwtToken);

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

    private String getEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationServiceException("토큰 값이 만료되었습니다.");
        } catch (Exception e) {
            throw new AccessDeniedException("토큰 형식이 올바르지 않습니다.");
        }
    }

}
