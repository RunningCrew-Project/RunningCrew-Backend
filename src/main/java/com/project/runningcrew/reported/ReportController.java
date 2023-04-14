package com.project.runningcrew.reported;

import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.reported.board.dto.request.CreateReportedBoardRequest;
import com.project.runningcrew.reported.board.dto.response.GetReportedBoardResponse;
import com.project.runningcrew.reported.comment.dto.request.CreateReportedCommentRequest;
import com.project.runningcrew.reported.comment.dto.response.GetReportedCommentResponse;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReportController {


    @Operation(
            summary = "게시글 신고하기",
            description = "게시글을 신고하여 신고정보를 생성한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/boards/report")
    public ResponseEntity<Void> reportBoard(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("crewId") Long crewId,
            @RequestBody @Valid CreateReportedBoardRequest createReportedBoardRequest
    ) {
        return null;
    }



    @Operation(
            summary = "댓글 신고하기",
            description = "댓글을 신고하여 신고정보를 생성한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/comments/report")
    public ResponseEntity<Void> reportComment(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("crewId") Long crewId,
            @RequestBody @Valid CreateReportedCommentRequest createReportedCommentRequest
    ) {
        return null;
    }



    @Operation(
            summary = "게시글 신고정보 목록 조회",
            description = "게시글 신고정보 목록을 조회한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/crews/{crewId}/boards/report")
    public ResponseEntity<PagingResponse<GetReportedBoardResponse>> getReportedBoards(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("crewId") Long crewId
    ) {
        return null;
    }



    @Operation(
            summary = "댓글 신고정보 목록 조회",
            description = "댓글 신고정보 목록을 조회한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/crews/{crewId}/comments/report")
    public ResponseEntity<PagingResponse<GetReportedCommentResponse>> getReportedComments(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("crewId") Long crewId
    ) {
        return null;
    }


}
