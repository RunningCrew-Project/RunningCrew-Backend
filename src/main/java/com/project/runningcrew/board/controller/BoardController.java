package com.project.runningcrew.board.controller;

import com.project.runningcrew.board.dto.request.CreateBoardRequest;
import com.project.runningcrew.board.dto.request.UpdateBoardRequest;
import com.project.runningcrew.board.dto.response.GetBoardResponse;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.board.service.BoardService;
import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.image.ImageService;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.resourceimage.service.BoardImageService;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "board", description = "게시글에 관한 api")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final BoardImageService boardImageService;
    private final MemberService memberService;
    private final CrewService crewService;


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


    //TODO 게시글 수정

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
        // 작성자 writer


        if(writer.equals(user)) {
            // 작성자 권한 & 게시글 타입 체크
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


    //TODO 게시글 삭제
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






    //TODO 자유 게시글 생성

    @PostMapping("/api/crews/{crewId}/boards/free")
    public ResponseEntity<CreateBoardRequest> createFreeBoard(
            @PathVariable("crewId") Long crewId,
            @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        Member member = memberService.findByUserAndCrew(user, crew);


    }






    //TODO 리뷰 게시글 생성

    //TODO 공지 게시글 생성



    //TODO 특정 멤버가 작성한 모든 게시글 정보 - 페이징

    //TODO 특정 키워드를 제목 or 내용에 포함하는 게시글 정보 - 페이징



    //TODO 자유 게시판 전체 게시글 정보 - 페이징

    //TODO 리뷰 게시판 전체 게시글 정보 - 페이징

    //TODO 공지 게시판 전체 게시글 정보 - 페이징




}
