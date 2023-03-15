package com.project.runningcrew.resourceimage.controller;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.service.BoardService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.resourceimage.dto.ImageListResponse;
import com.project.runningcrew.resourceimage.dto.SimpleImageDto;
import com.project.runningcrew.resourceimage.service.BoardImageService;
import com.project.runningcrew.resourceimage.service.RunningNoticeImageService;
import com.project.runningcrew.resourceimage.service.RunningRecordImageService;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.service.RunningNoticeService;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.runningrecord.service.RunningRecordService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Image", description = "이미지에 관한 api")
@RestController
@RequiredArgsConstructor
public class ResourceImageController {

    private final BoardService boardService;
    private final BoardImageService boardImageService;
    private final RunningRecordService runningRecordService;
    private final RunningRecordImageService runningRecordImageService;
    private final RunningNoticeService runningNoticeService;
    private final RunningNoticeImageService runningNoticeImageService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;


    @Operation(summary = "게시글 이미지 가져오기", description = "boardId 에 해당하는 게시글의 모든 이미지를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/boards/{boardId}/images")
    public ResponseEntity<ImageListResponse> getBoardImages(@PathVariable("boardId") Long boardId,
                                                            @Parameter(hidden = true) @CurrentUser User user) {
        Board board = boardService.findById(boardId);
        memberAuthorizationChecker.checkMember(user, board.getMember().getCrew());

        List<SimpleImageDto> simpleImageDtos = boardImageService.findAllByBoard(board).stream()
                .map(SimpleImageDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ImageListResponse(simpleImageDtos));
    }


    @Operation(summary = "런닝공지 이미지 가져오기", description = "runningRecordId 에 해당하는 런닝공지의 모든 이미지를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-notices/{runningNoticeId}/images")
    public ResponseEntity<ImageListResponse> getRunningNoticeImages(@PathVariable("runningNoticeId") Long runningNoticeId,
                                                            @Parameter(hidden = true) @CurrentUser User user) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        memberAuthorizationChecker.checkMember(user, runningNotice.getMember().getCrew());

        List<SimpleImageDto> simpleImageDtos = runningNoticeImageService.findAllByRunningNotice(runningNotice).stream()
                .map(SimpleImageDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ImageListResponse(simpleImageDtos));
    }


    @Operation(summary = "런닝기록 이미지 가져오기", description = "runningRecordId 에 해당하는 런닝기록의 모든 이미지를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/running-records/{runningRecordId}/images")
    public ResponseEntity<ImageListResponse> getRunningRecordImages(@PathVariable("runningRecordId") Long runningRecordId,
                                                                    @Parameter(hidden = true) @CurrentUser User user) {
        RunningRecord runningRecord = runningRecordService.findById(runningRecordId);

        List<SimpleImageDto> simpleImageDtos = runningRecordImageService.findAllByRunningRecord(runningRecord).stream()
                .map(SimpleImageDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ImageListResponse(simpleImageDtos));
    }

}
