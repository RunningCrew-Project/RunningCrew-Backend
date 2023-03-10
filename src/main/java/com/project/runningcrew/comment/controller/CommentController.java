package com.project.runningcrew.comment.controller;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.service.BoardService;
import com.project.runningcrew.comment.dto.request.CreateBoardCommentRequest;
import com.project.runningcrew.comment.dto.request.CreateRunningNoticeCommentRequest;
import com.project.runningcrew.comment.dto.response.GetCommentResponse;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.service.RunningNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.Current;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Tag(name = "comment", description = "댓글에 관한 api")
@RestController
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    private final BoardService boardService;
    private final RunningNoticeService runningNoticeService;
    private final MemberService memberService;

    private final String host = "localhost";




    @Operation(summary = "댓글 가져오기", description = "commentId 에 해당하는 댓글을 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCommentResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/comments/{commentId}")
    public ResponseEntity<GetCommentResponse> getComment(@PathVariable("commentId") Long commentId) {
        Comment comment = commentService.findById(commentId);
        return ResponseEntity.ok(new GetCommentResponse(comment));
    }




    @Operation(summary = "게시글 댓글 생성하기", description = "게시글 댓글을 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PostMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<Void> createBoardComment(
            @PathVariable("boardId") Long boardId,
            @ModelAttribute @Valid CreateBoardCommentRequest request
            // @CurrentUser User user
    ) {
        Board board = boardService.findById(boardId);
        Crew crew = board.getMember().getCrew();
        //Member member = memberService.findByUserAndCrew(user, crew);
        //BoardComment boardComment = new BoardComment(member, request.getDetail(), board);

//        Long commentId = commentService.saveComment(boardComment);
//        URI uri = UriComponentsBuilder.newInstance()
//                .scheme("http")
//                .host(host)
//                .path("/api/comments/{id}")
//                .build(commentId);

        return null;// ResponseEntity.created(uri).build();
    }



    @Operation(summary = "런닝 공지 댓글 생성하기", description = "런닝 공지 댓글을 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PostMapping("/api/running-notices/{runningNoticeId}/comments")
    public ResponseEntity<Void> createRunningNoticeComment(
            @PathVariable("runningNoticeId") Long runningNoticeId,
            @ModelAttribute @Valid CreateRunningNoticeCommentRequest request
    ) {
        Member member = memberService.findById(request.getMemberId());
        RunningNotice runningNotice =  runningNoticeService.findById(runningNoticeId);
        RunningNoticeComment runningNoticeComment = new RunningNoticeComment(member, request.getDetail(), runningNotice);

        Long commentId = commentService.saveComment(runningNoticeComment);
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .path("/api/comments/{id}")
                .build(commentId);

        return ResponseEntity.created(uri).build();
    }


    //TODO 댓글 수정기능



    //TODO 댓글 삭제기능



    //TODO 특정 게시글의 모든 댓글 정보 가져오기



    //TODO 특정 공지의 모든 댓글 정보 가져오기



    //TODO 특정 멤버가 작성한 모든 댓글 정보 가져오기 - 페이징



}
