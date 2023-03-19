package com.project.runningcrew.board.controller;

import com.project.runningcrew.board.dto.request.CreateNotReviewBoardRequest;
import com.project.runningcrew.board.dto.request.CreateReviewBoardRequest;
import com.project.runningcrew.board.dto.request.UpdateBoardRequest;
import com.project.runningcrew.board.dto.response.GetBoardResponse;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.board.service.BoardService;
import com.project.runningcrew.board.service.FreeBoardService;
import com.project.runningcrew.board.service.NoticeBoardService;
import com.project.runningcrew.board.service.ReviewBoardService;
import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.resourceimage.service.BoardImageService;
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
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "board", description = "게시글에 관한 api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final RunningRecordService runningRecordService;
    private final BoardImageService boardImageService;
    private final MemberService memberService;
    private final CrewService crewService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;

    private final FreeBoardService freeBoardService;
    private final NoticeBoardService noticeBoardService;
    private final ReviewBoardService reviewBoardService;


    @Value("${domain.name}")
    private String host;
    private int pagingSize = 15;



    @Operation(summary = "게시글 가져오기", description = "게시글 정보를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<GetBoardResponse> getBoard(
            @PathVariable("boardId") Long boardId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Board board = boardService.findById(boardId);
        Crew crew = board.getMember().getCrew();
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        int commentCount = commentService.countCommentAtBoard(board);
        return ResponseEntity.ok(new GetBoardResponse(board, commentCount));
    }




    @Operation(summary = "자유 게시글 생성하기", description = "자유 게시글을 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/boards/free", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createFreeBoard(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user,
            @ModelAttribute @Valid CreateNotReviewBoardRequest createNotReviewBoardRequest
    ) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        Member member = memberService.findByUserAndCrew(user, crew);
        FreeBoard freeBoard = new FreeBoard(member, createNotReviewBoardRequest.getTitle(), createNotReviewBoardRequest.getDetail());

        Long boardId = boardService.saveBoard(freeBoard, createNotReviewBoardRequest.getFiles());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/boards/{id}")
                .build(boardId);

        return ResponseEntity.created(uri).build();
    }



    @Operation(summary = "공지 게시글 생성하기", description = "공지 게시글을 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/boards/notice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createNoticeBoard(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user,
            @ModelAttribute @Valid CreateNotReviewBoardRequest createNotReviewBoardRequest
    ) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkManager(user, crew);
        //note 요청 user 의 크루 회원 여부 && Manager 이상의 등급 체크

        Member member = memberService.findByUserAndCrew(user, crew);

        NoticeBoard noticeBoard = new NoticeBoard(member, createNotReviewBoardRequest.getTitle(), createNotReviewBoardRequest.getDetail());
        Long boardId = noticeBoardService.saveNoticeBoard(noticeBoard, createNotReviewBoardRequest.getFiles());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/boards/{id}")
                .build(boardId);

        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "리뷰 게시글 생성하기", description = "리뷰 게시글을 생성한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/crews/{crewId}/boards/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createReviewBoard(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody @Valid CreateReviewBoardRequest createReviewBoardRequest
    ) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        Member member = memberService.findByUserAndCrew(user, crew);
        RunningRecord runningRecord = runningRecordService.findById(createReviewBoardRequest.getRunningRecordId());
        ReviewBoard reviewBoard = new ReviewBoard(member, createReviewBoardRequest.getTitle(), createReviewBoardRequest.getDetail(), runningRecord);

        Long boardId = boardService.saveBoard(reviewBoard, createReviewBoardRequest.getFiles());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/boards/{id}")
                .build(boardId);

        return ResponseEntity.created(uri).build();
    }



    @Operation(summary = "게시글 수정하기", description = "게시글 정보를 수정한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PutMapping(value = "/api/boards/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> changeBoard(
            @PathVariable("boardId") Long boardId,
            @Parameter(hidden = true) @CurrentUser User user,
            @ModelAttribute @Valid UpdateBoardRequest updateBoardRequest
    ) {

        Board board = boardService.findById(boardId);
        Long memberId = board.getMember().getId();
        Crew crew = board.getMember().getCrew();


        if(board instanceof FreeBoard) {
            memberAuthorizationChecker.checkAuthOnlyUser(user, crew, memberId);
            //note 해당 유저의 크루 가입 여부 && 게시글 수정 권한 확인

            FreeBoard newBoard = new FreeBoard(board.getMember(), updateBoardRequest.getTitle(), updateBoardRequest.getDetail());

            List<Long> idList = updateBoardRequest.getDeleteFiles();
            List<BoardImage> deleteFiles = idList.stream().map(boardImageService::findById).collect(Collectors.toList());
            boardService.updateBoard(board, newBoard, updateBoardRequest.getAddFiles(), deleteFiles);

        } else if (board instanceof NoticeBoard) {
            memberAuthorizationChecker.checkAuthUserAndManger(user, crew, memberId);
            //note 해당 유저의 크루 가입 여부 && 게시글 수정 권한 && 매니저 이상 등급 확인

            NoticeBoard newBoard = new NoticeBoard(board.getMember(), updateBoardRequest.getTitle(), updateBoardRequest.getDetail());

            List<Long> idList = updateBoardRequest.getDeleteFiles();
            List<BoardImage> deleteFiles = idList.stream().map(boardImageService::findById).collect(Collectors.toList());
            boardService.updateBoard(board, newBoard, updateBoardRequest.getAddFiles(), deleteFiles);

        } else if (board instanceof ReviewBoard) {
            memberAuthorizationChecker.checkAuthOnlyUser(user, crew, memberId);
            //note 해당 유저의 크루 가입 여부 && 게시글 수정 권한 확인

            ReviewBoard newBoard = new ReviewBoard(board.getMember(), updateBoardRequest.getTitle(), updateBoardRequest.getDetail(), ((ReviewBoard) board).getRunningRecord());

            List<Long> idList = updateBoardRequest.getDeleteFiles();
            List<BoardImage> deleteFiles = idList.stream().map(boardImageService::findById).collect(Collectors.toList());
            boardService.updateBoard(board, newBoard, updateBoardRequest.getAddFiles(), deleteFiles);

        }

        return ResponseEntity.noContent().build();

    }



    @Operation(summary = "게시글 삭제하기", description = "게시글을 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/boards/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable("boardId") Long boardId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Board board = boardService.findById(boardId);
        Crew crew = board.getMember().getCrew();
        Long memberId = board.getMember().getId();

        memberAuthorizationChecker.checkAuthOnlyUser(user, crew, memberId);
        //note 해당 유저의 크루 가입 여부 && 글 삭제 권한 확인

        boardService.deleteBoard(board);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "특정 작성자의 모든 게시글 정보 가져오기"
            , description = "특정 작성자의 모든 게시글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")}
    )
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
    @GetMapping("/api/members/{memberId}/boards")
    public ResponseEntity<PagingResponse<SimpleBoardDto>> getBoardsOfMember(
            @RequestParam("page") int page,
            @PathVariable("memberId") Long memberId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Member member = memberService.findById(memberId);

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<Board> boardSlice = boardService.findBoardByMember(member, pageRequest);


        List<Long> boardIds = boardSlice.stream().map(Board::getId).collect(Collectors.toList());
        Map<Long, Long> countMaps = commentService.countAllByBoardIds(boardIds);
        Map<Long, String> imageMaps = boardImageService.findFirstImageUrls(boardIds);


        List<SimpleBoardDto> dtoList = new ArrayList<>();

        for (Long boardId : boardIds) {
            Board board = boardService.findById(boardId);
            String fileName = imageMaps.get(boardId);
            Long commentCount = countMaps.get(boardId);
            dtoList.add(new SimpleBoardDto(board, fileName, commentCount));
        }

        return ResponseEntity.ok(new PagingResponse<>(
                new SliceImpl<>(dtoList, boardSlice.getPageable(), boardSlice.hasNext())));

    }



    @Operation(summary = "특정 키워드를 포함한 모든 게시글 정보 가져오기"
            , description = "특정 키워드를 포함한 모든 게시글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")}
    )
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
    @GetMapping("/api/crews/{crewId}/boards")
    public ResponseEntity<PagingResponse<SimpleBoardDto>> getBoardsOfKeyword(
            @RequestParam("page") int page,
            @RequestParam("keyword") String keyword,
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {

        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증


        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<Board> boardSlice = boardService.findBoardByCrewAndKeyWord(crew, keyword, pageRequest);

        List<Long> boardIds = boardSlice.stream().map(Board::getId).collect(Collectors.toList());
        Map<Long, Long> countMaps = commentService.countAllByBoardIds(boardIds);
        Map<Long, String> imageMaps = boardImageService.findFirstImageUrls(boardIds);


        List<SimpleBoardDto> dtoList = new ArrayList<>();

        for (Long boardId : boardIds) {
            Board board = boardService.findById(boardId);
            String fileName = imageMaps.get(boardId);
            Long commentCount = countMaps.get(boardId);
            dtoList.add(new SimpleBoardDto(board, fileName, commentCount));
        }

        //note dtoList -> Slice
        Slice<SimpleBoardDto> responseSlice = new SliceImpl<>(dtoList, boardSlice.getPageable(), boardSlice.hasNext());
        return ResponseEntity.ok(new PagingResponse<>(responseSlice));

    }


    @Operation(summary = "특정 크루의 자유 게시판 정보 가져오기"
            , description = "자유 게시판의 모든 게시글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")}
    )
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
    @GetMapping("/api/crews/{crewId}/boards/free")
    public ResponseEntity<PagingResponse<SimpleBoardDto>> getSliceOfFreeBoards(
        @PathVariable("crewId") Long crewId,
        @RequestParam("page") int page,
        @Parameter(hidden = true) @CurrentUser User user
    ){
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<FreeBoard> boardSlice = freeBoardService.findFreeBoardByCrew(crew, pageRequest);

        List<Long> boardIds = boardSlice.stream().map(Board::getId).collect(Collectors.toList());
        Map<Long, Long> countMaps = commentService.countAllByBoardIds(boardIds);
        Map<Long, String> imageMaps = boardImageService.findFirstImageUrls(boardIds);


        List<SimpleBoardDto> dtoList = new ArrayList<>();

        for (Long boardId : boardIds) {
            Board board = boardService.findById(boardId);
            String fileName = imageMaps.get(boardId);
            Long commentCount = countMaps.get(boardId);
            dtoList.add(new SimpleBoardDto(board, fileName, commentCount));
        }

        //note dtoList -> Slice
        Slice<SimpleBoardDto> responseSlice = new SliceImpl<>(dtoList, boardSlice.getPageable(), boardSlice.hasNext());
        return ResponseEntity.ok(new PagingResponse<>(responseSlice));

    }



    @Operation(summary = "특정 크루의 공지 게시판 정보 가져오기"
            , description = "공지 게시판의 모든 게시글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")}
    )
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
    @GetMapping("/api/crews/{crewId}/boards/notice")
    public ResponseEntity<PagingResponse<SimpleBoardDto>> getSliceOfNoticeBoards(
            @PathVariable("crewId") Long crewId,
            @RequestParam("page") int page,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<NoticeBoard> boardSlice = noticeBoardService.findNoticeBoardByCrew(crew, pageRequest);

        List<Long> boardIds = boardSlice.stream().map(Board::getId).collect(Collectors.toList());
        Map<Long, Long> countMaps = commentService.countAllByBoardIds(boardIds);
        Map<Long, String> imageMaps = boardImageService.findFirstImageUrls(boardIds);


        List<SimpleBoardDto> dtoList = new ArrayList<>();

        for (Long boardId : boardIds) {
            Board board = boardService.findById(boardId);
            String fileName = imageMaps.get(boardId);
            Long commentCount = countMaps.get(boardId);
            dtoList.add(new SimpleBoardDto(board, fileName, commentCount));
        }

        //note dtoList -> Slice
        Slice<SimpleBoardDto> responseSlice = new SliceImpl<>(dtoList, boardSlice.getPageable(), boardSlice.hasNext());
        return ResponseEntity.ok(new PagingResponse<>(responseSlice));

    }


    @Operation(summary = "특정 크루의 리뷰 게시판 정보 가져오기"
            , description = "리뷰 게시판의 모든 게시글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")}
    )
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
    @GetMapping("/api/crews/{crewId}/boards/review")
    public ResponseEntity<PagingResponse<SimpleBoardDto>> getSliceOfReviewBoards(
            @PathVariable("crewId") Long crewId,
            @RequestParam("page") int page,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkMember(user, crew);
        //note 요청 user 의 크루 회원 여부 검증

        PageRequest pageRequest = PageRequest.of(page, pagingSize);
        Slice<ReviewBoard> boardSlice = reviewBoardService.findReviewBoardByCrew(crew, pageRequest);

        List<Long> boardIds = boardSlice.stream().map(Board::getId).collect(Collectors.toList());
        Map<Long, Long> countMaps = commentService.countAllByBoardIds(boardIds);
        Map<Long, String> imageMaps = boardImageService.findFirstImageUrls(boardIds);


        List<SimpleBoardDto> dtoList = new ArrayList<>();

        for (Long boardId : boardIds) {
            Board board = boardService.findById(boardId);
            String fileName = imageMaps.get(boardId);
            Long commentCount = countMaps.get(boardId);
            dtoList.add(new SimpleBoardDto(board, fileName, commentCount));
        }

        //note dtoList -> Slice
        Slice<SimpleBoardDto> responseSlice = new SliceImpl<>(dtoList, boardSlice.getPageable(), boardSlice.hasNext());
        return ResponseEntity.ok(new PagingResponse<>(responseSlice));

    }


}
