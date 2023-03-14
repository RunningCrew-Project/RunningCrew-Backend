package com.project.runningcrew.board.controller;

import com.project.runningcrew.board.dto.request.CreateBoardRequest;
import com.project.runningcrew.board.dto.request.UpdateBoardRequest;
import com.project.runningcrew.board.dto.response.GetBoardResponse;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.board.service.BoardService;
import com.project.runningcrew.board.service.NoticeBoardService;
import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.PagingResponse;
import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.common.dto.SimpleBoardOfCommentDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.image.ImageService;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.resourceimage.service.BoardImageService;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.runningrecord.service.RunningRecordService;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "board", description = "게시글에 관한 api")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final RunningRecordService runningRecordService;
    private final NoticeBoardService noticeBoardService;
    private final BoardImageService boardImageService;
    private final MemberService memberService;
    private final CrewService crewService;

    @Value("${domain.name}")
    private String host;

    private int pagingSize = 10;



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
            @CurrentUser User user
    ) {
        // user 검증 필요(?)
        Board board = boardService.findById(boardId);
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
            @CurrentUser User user,
            @ModelAttribute @Valid CreateBoardRequest request
    ) {
        Crew crew = crewService.findById(crewId);
        Member member = memberService.findByUserAndCrew(user, crew);

        FreeBoard freeBoard = new FreeBoard(member, request.getTitle(), request.getDetail());
        Long boardId = boardService.saveBoard(freeBoard, request.getFiles());
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
    @PostMapping("/api/crews/{crewId}/boards/notice")
    public ResponseEntity<Void> createNoticeBoard(
            @PathVariable("crewId") Long crewId,
            @CurrentUser User user,
            @ModelAttribute @Valid CreateBoardRequest request
    ) {
        Crew crew = crewService.findById(crewId);
        Member member = memberService.findByUserAndCrew(user, crew);

        NoticeBoard noticeBoard = new NoticeBoard(member, request.getTitle(), request.getDetail());
        Long boardId = noticeBoardService.saveNoticeBoard(noticeBoard, request.getFiles());
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
    @PostMapping("/api/crews/{crewId}/boards/review")
    public ResponseEntity<Void> createReviewBoard(
            @PathVariable("crewId") Long crewId,
            @CurrentUser User user,
            @ModelAttribute @Valid CreateBoardRequest request
    ) {
        Crew crew = crewService.findById(crewId);
        Member member = memberService.findByUserAndCrew(user, crew);
        RunningRecord runningRecord = runningRecordService.findById(request.getRunningRecordId());

        ReviewBoard reviewBoard = new ReviewBoard(member, request.getTitle(), request.getDetail(), runningRecord);
        Long boardId = boardService.saveBoard(reviewBoard, request.getFiles());
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
            @CurrentUser User user,
            @ModelAttribute @Valid UpdateBoardRequest request
    ) {

        Board originBoard = boardService.findById(boardId);
        Map<Long, BoardImage> firstImages = boardImageService.findFirstImages(request.getDeleteFiles());

        List<BoardImage> deleteFiles = new ArrayList<>();
        for (Long key : firstImages.keySet()) {
            deleteFiles.add(firstImages.get(key));
        }

        User writer = originBoard.getMember().getUser();
        //note 게시글 작성자 : writer


        if(writer.equals(user)) {
            //note 작성자 권한 & 게시글 타입 체크
            if(originBoard instanceof FreeBoard) {
                FreeBoard newBoard = new FreeBoard(originBoard.getMember(), request.getTitle(), request.getDetail());
                boardService.updateBoard(originBoard, newBoard, request.getAddFiles(), deleteFiles);
            } else if (originBoard instanceof NoticeBoard) {
                NoticeBoard newBoard = new NoticeBoard(originBoard.getMember(), request.getTitle(), request.getDetail());
                boardService.updateBoard(originBoard, newBoard, request.getAddFiles(), deleteFiles);
            } else if (originBoard instanceof ReviewBoard) {
                ReviewBoard newBoard =
                        new ReviewBoard(originBoard.getMember(), request.getTitle(), request.getDetail(),
                                ((ReviewBoard) originBoard).getRunningRecord());
                boardService.updateBoard(originBoard, newBoard, request.getAddFiles(), deleteFiles);
            }
            return ResponseEntity.noContent().build();
        } else {
            throw new AccessDeniedException("게시글 작성자만 수정할 수 있습니다.");
        }

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
            @CurrentUser User user
    ) {
        Board board = boardService.findById(boardId);
        User writer = board.getMember().getUser();

        if(writer.equals(user)) {
            boardService.deleteBoard(board);
            return ResponseEntity.noContent().build();
        } else {
            throw new AccessDeniedException("게시글 작성자만 삭제할 수 있습니다.");
        }

    }



    @Operation(summary = "특정 작성자의 모든 게시글 정보 가져오기"
            , description = "특정 작성자의 모든 게시글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
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
            @CurrentUser User user
    ) {
        Member member = memberService.findById(memberId);
        User writer = member.getUser();
        if (writer.equals(user)) {
            //note 작성자 권한 검증
            PageRequest pageRequest = PageRequest.of(page, pagingSize);

            Slice<Board> boardSlice = boardService.findBoardByMember(member, pageRequest);
            List<Long> boardIdList = new ArrayList<>();
            for (Board board : boardSlice) { boardIdList.add(board.getId()); }
            //note idList 를 만들어 commentCountList 를 뽑아낸다.

            List<Integer> commentCountList = commentService.countByBoardIdList(boardIdList);
            List<SimpleBoardDto> dtoList = new ArrayList<>();

            for (int i = 0; i < commentCountList.size(); i++) {
                dtoList.add(new SimpleBoardDto(boardSlice.getContent().get(i), commentCountList.get(i)));
            }

            Slice<SimpleBoardDto> responseSlice = new SliceImpl<>(dtoList);
            //note dtoList -> Slice

            return ResponseEntity.ok(new PagingResponse<>(responseSlice));

        } else {
            throw new AccessDeniedException("게시글 작성자만 확인할 수 있습니다.");
        }
    }



    @Operation(summary = "특정 키워드를 포함한 모든 게시글 정보 가져오기"
            , description = "특정 키워드를 포함한 모든 게시글을 가져온다."
            , security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
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
            @CurrentUser User user
    ) {

        Crew crew = crewService.findById(crewId);
        Slice<Board> boardSlice = boardService.findBoardByCrewAndKeyWord(crew, keyword);

        List<Long> boardIdList = new ArrayList<>();
        for (Board board : boardSlice) { boardIdList.add(board.getId()); }
        //note idList 를 만들어 commentCountList 를 뽑아낸다.


        List<Integer> commentCountList = commentService.countByBoardIdList(boardIdList);
        List<SimpleBoardDto> dtoList = new ArrayList<>();

        for (int i = 0; i < commentCountList.size(); i++) {
            dtoList.add(new SimpleBoardDto(boardSlice.getContent().get(i), commentCountList.get(i)));
        }

        Slice<SimpleBoardDto> responseSlice = new SliceImpl<>(dtoList);
        //note dtoList -> Slice

        return ResponseEntity.ok(new PagingResponse<>(responseSlice));

    }



    //TODO 자유 게시판 전체 게시글 정보 - 페이징

    //TODO 리뷰 게시판 전체 게시글 정보 - 페이징

    //TODO 공지 게시판 전체 게시글 정보 - 페이징




}
