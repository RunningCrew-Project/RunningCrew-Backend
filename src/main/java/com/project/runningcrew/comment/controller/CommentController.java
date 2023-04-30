package com.project.runningcrew.comment.controller;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.service.BoardService;
import com.project.runningcrew.comment.dto.request.ChangeCommentRequest;
import com.project.runningcrew.comment.dto.request.CreateBoardCommentRequest;
import com.project.runningcrew.comment.dto.request.CreateRunningNoticeCommentRequest;
import com.project.runningcrew.comment.dto.response.CommentListResponse;
import com.project.runningcrew.comment.dto.response.GetCommentResponse;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.common.dto.SimpleCommentDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.resourceimage.service.BoardImageService;
import com.project.runningcrew.resourceimage.service.RunningNoticeImageService;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.service.RunningNoticeService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Comment", description = "댓글에 관한 api")
@RestController
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    private final BoardService boardService;
    private final RunningNoticeService runningNoticeService;
    private final MemberService memberService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;

    @Value("${domain.name}")
    private String host;
    private final int pagingSize = 15;



    @Operation(
            summary = "댓글 상세조회",
            description = "commentId 에 해당하는 댓글의 상세정보를 가져온다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
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
    public ResponseEntity<GetCommentResponse> getComment(
            @PathVariable("commentId") Long commentId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Comment comment = commentService.findById(commentId);
        Crew crew = comment.getMember().getCrew();
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

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
            @RequestBody @Valid CreateBoardCommentRequest createBoardCommentRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Board board = boardService.findById(boardId);
        Crew crew = board.getMember().getCrew();
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        Member member = memberService.findByUserAndCrew(user, crew);
        BoardComment boardComment = new BoardComment(member, createBoardCommentRequest.getDetail(), board);

        Long commentId = commentService.saveComment(boardComment);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/comments/{id}")
                .build(commentId);

        return ResponseEntity.created(uri).build();
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
            @RequestBody @Valid CreateRunningNoticeCommentRequest createRunningNoticeCommentRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        RunningNotice runningNotice =  runningNoticeService.findById(runningNoticeId);
        Crew crew = runningNotice.getMember().getCrew();
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        Member member = memberService.findByUserAndCrew(user, crew);
        RunningNoticeComment runningNoticeComment = new RunningNoticeComment(member, createRunningNoticeCommentRequest.getDetail(), runningNotice);

        Long commentId = commentService.saveComment(runningNoticeComment);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/comments/{id}")
                .build(commentId);

        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "댓글 수정하기", description = "댓글을 수정한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> changeComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody @Valid ChangeCommentRequest changeCommentRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Comment comment = commentService.findById(commentId);
        Crew crew = comment.getMember().getCrew();
        Long memberId = comment.getMember().getId();
        memberAuthorizationChecker.checkAuthOnlyUser(user, crew, memberId);
        //note 요청 user 의 크루 회원 여부 && 댓글 권한 검증

        commentService.changeComment(comment, changeCommentRequest.getDetail());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "댓글 삭제하기", description = "댓글을 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("commentId") Long commentId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Comment comment = commentService.findById(commentId);
        Crew crew = comment.getMember().getCrew();
        Long memberId = comment.getMember().getId();
        memberAuthorizationChecker.checkAuthOnlyUser(user, crew, memberId);
        //note 요청 user 의 크루 회원 여부 && 댓글 권한 검증

        commentService.deleteComment(comment);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "특정 게시글의 모든 댓글 가져오기"
            , description = "특정 게시글의 모든 댓글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<CommentListResponse> getCommentListOfBoard(
            @PathVariable("boardId") Long boardId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Board board = boardService.findById(boardId);
        Crew crew = board.getMember().getCrew();
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        List<SimpleCommentDto> dtoList = commentService.findAllByBoard(board);
        int commentCount = dtoList.size();
        return ResponseEntity.ok(new CommentListResponse(commentCount, dtoList));
    }


    @Operation(summary = "특정 런닝 공지의 모든 댓글 가져오기"
            , description = "특정 런닝 공지의 모든 댓글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/running-notices/{runningNoticeId}/comments")
    public ResponseEntity<CommentListResponse> getCommentListOfRunningNotice(
            @PathVariable("runningNoticeId") Long runningNoticeId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        Crew crew = runningNotice.getMember().getCrew();
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        List<SimpleCommentDto> dtoList = commentService.findAllByRunningNotice(runningNotice);
        int commentCount = dtoList.size();
        return ResponseEntity.ok(new CommentListResponse(commentCount, dtoList));
    }


}
