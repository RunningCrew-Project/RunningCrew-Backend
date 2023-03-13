package com.project.runningcrew.comment.controller;

import com.amazonaws.services.cloudformation.model.Change;
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
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.service.RunningNoticeService;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "comment", description = "댓글에 관한 api")
@RestController
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    private final BoardService boardService;
    private final RunningNoticeService runningNoticeService;
    private final MemberService memberService;
    private final UserService userService;
    private int pagingSize = 10;

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
            @ModelAttribute @Valid CreateBoardCommentRequest request,
            @CurrentUser User user
    ) {
        Board board = boardService.findById(boardId);
        Member member = memberService.findByUserAndCrew(user, board.getMember().getCrew());
        BoardComment boardComment = new BoardComment(member, request.getDetail(), board);

        Long commentId = commentService.saveComment(boardComment);
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
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
            @ModelAttribute @Valid CreateRunningNoticeCommentRequest request,
            @CurrentUser User user
    ) {
        RunningNotice runningNotice =  runningNoticeService.findById(runningNoticeId);
        Member member = memberService.findByUserAndCrew(user, runningNotice.getMember().getCrew());
        RunningNoticeComment runningNoticeComment = new RunningNoticeComment(member, request.getDetail(), runningNotice);

        Long commentId = commentService.saveComment(runningNoticeComment);
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
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
            @ModelAttribute @Valid ChangeCommentRequest request,
            @CurrentUser User user
    ) {
        Comment comment = commentService.findById(commentId);
        User writer = comment.getMember().getUser();

        if(user.equals(writer)) {
            commentService.changeComment(comment, request.getDetail());
            return ResponseEntity.noContent().build();
        } else {
            throw new AccessDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
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
            @CurrentUser User user
    ) {
        Comment comment = commentService.findById(commentId);
        User writer = comment.getMember().getUser();

        if(user.equals(writer)) {
            commentService.deleteComment(comment);
            return ResponseEntity.noContent().build();
            // user 의 comment 삭제 권한 검증
        } else {
            throw new AccessDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }
    }


    @Operation(summary = "특정 게시글의 모든 댓글 가져오기"
            , description = "특정 게시글의 모든 댓글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<CommentListResponse<SimpleCommentDto>> getCommentListOfBoard(
            @PathVariable("boardId") Long boardId
    ) {
        Board board = boardService.findById(boardId);
        List<BoardComment> commentList = commentService.findAllByBoard(board);
        int commentCount = commentService.countCommentAtBoard(board);
        // 게시글의 댓글 수 정보 -> commentCount

        List<SimpleCommentDto> simpleCommentDtos = commentList.stream().map(SimpleCommentDto::new)
                .collect(Collectors.toList());
        // 각 댓글의 정보 -> SimpleCommentDtos

        return ResponseEntity.ok(new CommentListResponse<>(commentCount, simpleCommentDtos));
    }


    @Operation(summary = "특정 런닝 공지의 모든 댓글 가져오기"
            , description = "특정 런닝 공지의 모든 댓글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/running-notices/{runningNoticeId}/comments")
    public ResponseEntity<CommentListResponse<SimpleCommentDto>> getCommentListOfRunningNotice(
            @PathVariable("runningNoticeId") Long runningNoticeId
    ) {
        RunningNotice runningNotice = runningNoticeService.findById(runningNoticeId);
        List<RunningNoticeComment> commentList = commentService.findAllByRunningNotice(runningNotice);
        int commentCount = commentService.countCommentAtRunningNotice(runningNotice);
        // 런닝 공지의 댓글 수 정보 -> commentCount

        List<SimpleCommentDto> simpleCommentDtos = commentList.stream().map(SimpleCommentDto::new)
                .collect(Collectors.toList());
        // 각 댓글의 정보 -> SimppleCommentDtos

        return ResponseEntity.ok(new CommentListResponse<>(commentCount, simpleCommentDtos));
    }


    @Operation(summary = "특정 멤버가 작성한 모든 댓글 정보 가져오기", description = "특정 멤버가 작성한 모든 댓글 정보를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagingResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/members/{memberId}/comments")
    public ResponseEntity<PagingResponse<SimpleCommentDto>> getCommentPageOfMember(
            @RequestParam("page") int page,
            @PathVariable("memberId") Long memberId,
            @CurrentUser User user
    ) {
        Member member = memberService.findById(memberId);
        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<Comment> commentSlice = commentService.findAllByMember(member, pageRequest);

        SliceImpl<SimpleCommentDto> responseSlice = commentSlice.stream().map(SimpleCommentDto::new).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new SliceImpl<>(list, pageRequest, commentSlice.hasNext())
        ));

        return ResponseEntity.ok(new PagingResponse<>(responseSlice));
    }




}
