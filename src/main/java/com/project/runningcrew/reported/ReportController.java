package com.project.runningcrew.reported;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.service.BoardService;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.reported.totalpost.board.ReportedBoard;
import com.project.runningcrew.reported.totalpost.board.dto.CreateReportedBoardRequest;
import com.project.runningcrew.reported.comment.ReportedComment;
import com.project.runningcrew.reported.comment.dto.request.CreateReportedCommentRequest;
import com.project.runningcrew.reported.comment.dto.response.GetReportedCommentResponse;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.reported.totalpost.runningnotice.ReportedRunningNotice;
import com.project.runningcrew.reported.totalpost.runningnotice.dto.CreateReportedRunningNoticeRequest;
import com.project.runningcrew.reported.totalpost.ReportedTotalPost;
import com.project.runningcrew.reported.totalpost.GetReportedTotalPostResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Report", description = "신고 기능에 관한 api")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ReportController {

    private final CrewService crewService;
    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final RunningNoticeService runningNoticeService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;

    private final ReportService reportService;
    private int pagingSize = 15;

    @Value("${domain.name}")
    private String host;



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
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        Member reporter = memberService.findById(createReportedBoardRequest.getReporterMemberId());
        Board board = boardService.findById(createReportedBoardRequest.getBoardId());
        ReportType reportType = ReportType.getReportType(createReportedBoardRequest.getReportType());
        reportService.saveReportedBoard(new ReportedBoard(board, reporter, reportType));

        return ResponseEntity.created(null).build();
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
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        Member reporter = memberService.findById(createReportedCommentRequest.getReporterMemberId());
        Comment comment = commentService.findById(createReportedCommentRequest.getCommentId());
        ReportType reportType = ReportType.getReportType(createReportedCommentRequest.getReportType());
        reportService.saveReportedComment(new ReportedComment(comment, reporter, reportType));

        return ResponseEntity.created(null).build();
    }



    @Operation(
            summary = "런닝 공지 신고하기",
            description = "런닝 공지를 신고하여 신고정보를 생성한다.",
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
    @PostMapping(value = "/api/crews/{crewId}/running-notices/report")
    public ResponseEntity<Void> reportRunningNotice(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("crewId") Long crewId,
            @RequestBody @Valid CreateReportedRunningNoticeRequest createReportedRunningNoticeRequest
    ) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);

        Member reporter = memberService.findById(createReportedRunningNoticeRequest.getReporterMemberId());
        RunningNotice runningNotice = runningNoticeService.findById(createReportedRunningNoticeRequest.getRunningNoticeId());
        ReportType reportType = ReportType.getReportType(createReportedRunningNoticeRequest.getReportType());
        reportService.saveReportedRunningNotice(new ReportedRunningNotice(runningNotice, reporter, reportType));

        return ResponseEntity.created(null).build();
    }


    @Operation(
            summary = "크루 신고글 정보 목록 조회",
            description = "게시글 신고글 정보 목록을 조회한다.",
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
    @GetMapping(value = "/api/crews/{crewId}/total-posts/report")
    public ResponseEntity<PagingResponse<GetReportedTotalPostResponse>> getReportedBoards(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("crewId") Long crewId,
            @RequestParam("page") @PositiveOrZero int page
    ) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkLeader(user, crew);
        //note Leader 체크

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<ReportedTotalPost> findSlice = reportService.findReportedTotalPostsByCrew(crew, pageRequest);

        List<GetReportedTotalPostResponse> dtoList = new ArrayList<>();
        for (ReportedTotalPost reportedTotalPost : findSlice) {
            if(reportedTotalPost instanceof ReportedBoard) {

                ReportedBoard reportedBoard = (ReportedBoard) reportedTotalPost;
                dtoList.add(new GetReportedTotalPostResponse(
                        reportedTotalPost.getId(),
                        reportedBoard.getId(),
                        "board",
                        reportedBoard.getReportType().toString()
                ));
            } else if (reportedTotalPost instanceof ReportedRunningNotice) {

                ReportedRunningNotice reportedRunningNotice = (ReportedRunningNotice) reportedTotalPost;
                dtoList.add(new GetReportedTotalPostResponse(
                        reportedTotalPost.getId(),
                        reportedRunningNotice.getId(),
                        "runningNotice",
                        reportedRunningNotice.getReportType().toString()
                ));
            }
        }

        Slice<GetReportedTotalPostResponse> dtoSlice = new SliceImpl<>(dtoList, findSlice.getPageable(), findSlice.hasNext());
        return ResponseEntity.ok(new PagingResponse<>(dtoSlice));
    }



    @Operation(
            summary = "크루 댓글 신고정보 목록 조회",
            description = "크루 댓글 신고정보 목록을 조회한다.",
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
    @GetMapping(value = "/api/crews/{crewId}/comments/report")
    public ResponseEntity<PagingResponse<GetReportedCommentResponse>> getReportedComments(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("crewId") Long crewId,
            @RequestParam("page") @PositiveOrZero int page
    ) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkLeader(user, crew);
        //note Leader 체크

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<ReportedComment> findSlice = reportService.findReportedCommentsByCrew(crew, pageRequest);

        List<GetReportedCommentResponse> dtoList = findSlice.stream().map(element -> new GetReportedCommentResponse(
                element.getId(),
                element.getComment().getId(),
                element.getReportType()
        )).collect(Collectors.toList());

        Slice<GetReportedCommentResponse> dtoSlice = new SliceImpl<>(dtoList, findSlice.getPageable(), findSlice.hasNext());
        return ResponseEntity.ok(new PagingResponse<>(dtoSlice));
    }


}
