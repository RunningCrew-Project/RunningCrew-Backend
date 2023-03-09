package com.project.runningcrew.refreshtoken.service;

import com.project.runningcrew.exception.JwtVerificationException;
import com.project.runningcrew.exception.notFound.RefreshTokenNotFoundException;
import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.refreshtoken.dto.TokensDto;
import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.refreshtoken.repository.RefreshTokenRepository;
import com.project.runningcrew.security.JwtProvider;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtProvider jwtProvider;
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    /**
     * refreshToken 을 확인하고, user 의 refreshToken 이 맞다면 accessToken 을 생성하여 TokenDto 에 넣는다.
     * refreshToken 의 만료일이 3일 이내라면 refreshToken 을 업데이트하고 TokenDto 에 넣는다.
     *
     * @param user 로그인한 유저
     * @param refreshToken 로그인한 유저의 refreshToken
     * @return TokenDto
     */
    @Transactional
    public TokensDto refresh(User user, String refreshToken) {

        TokensDto tokensDto = new TokensDto();
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(refreshToken).getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtVerificationException("토큰 값이 만료되었습니다.");
        } catch (Exception e) {
            throw new JwtVerificationException("토큰 형식이 올바르지 않습니다.");
        }

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUser(user)
                .orElseThrow(RefreshTokenNotFoundException::new);
        if (!refreshTokenEntity.getRefreshToken().equals(refreshToken)) {
            throw new JwtVerificationException("유효하지 않은 리프레시 토큰입니다.");
        }
        UserRole userRole = userRoleRepository.findByUser(user).orElseThrow(UserRoleNotFoundException::new);
        String accessToken = jwtProvider.createAccessToken(user, userRole);
        tokensDto.setAccessToken(accessToken);

        long expiration = claims.getExpiration().getTime();
        long now = new Date().getTime();
        long diffTime = now - expiration;

        if (diffTime < 86400000) {
            String newRefreshToken = jwtProvider.createRefreshToken(user);
            refreshTokenEntity.updateRefreshToken(newRefreshToken);
            tokensDto.setRefreshToken(refreshToken);
        }

        return tokensDto;
    }

}
