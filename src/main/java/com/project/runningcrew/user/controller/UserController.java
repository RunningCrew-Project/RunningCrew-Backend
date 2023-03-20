package com.project.runningcrew.user.controller;

import com.project.runningcrew.comment.dto.response.GetCommentResponse;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.user.dto.response.GetUserResponse;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "user 에 관한 api", name = "user")
@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @Operation(summary = "유저 정보 가져오기", description = "유저 정보를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<GetUserResponse> getUser(
            @PathVariable("userId") Long userId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        User findUser = userService.findById(userId);
        return ResponseEntity.ok(new GetUserResponse(findUser));
    }







}
