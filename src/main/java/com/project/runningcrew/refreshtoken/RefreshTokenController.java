package com.project.runningcrew.refreshtoken;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exception.AuthorizationException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.refreshtoken.dto.TokensDto;
import com.project.runningcrew.refreshtoken.service.RefreshTokenService;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "RefreshToken", description = "리프레시 토큰에 관한 api")
@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/refresh")
    public ResponseEntity<Void> refresh(@Parameter(hidden = true) @CurrentUser User user,
                                        HttpServletRequest request) {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            throw new AuthorizationException();
        }

        String refreshToken = jwtHeader.replace("Bearer ", "");
        TokensDto tokensDto = refreshTokenService.refresh(user, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokensDto.getAccessToken());

        if (tokensDto.getRefreshToken() != null) {
            ResponseCookie cookie = ResponseCookie
                    .from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(60 * 60 * 24 * 14)
                    .path("/")
                    .build();
            headers.set(HttpHeaders.COOKIE, cookie.toString());
        }

        return ResponseEntity.ok().headers(headers).build();
    }

}
