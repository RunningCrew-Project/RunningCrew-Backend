package com.project.runningcrew.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.common.dto.SimpleMemberDto;
import com.project.runningcrew.common.dto.SimpleRunningRecordDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class GetBoardResponse {

    @Schema(description = "게시글 아이디", example = "1")
    private Long id;

    @Schema(description = "게시글 타입", example = "free")
    private String type;

    @Schema(description = "게시글의 댓글 수", example = "3")
    private int commentCount;

    @Schema(description = "게시글 작성자 정보")
    @JsonProperty("member")
    private SimpleMemberDto simpleMemberDto;

    @Schema(description = "게시글 제목", example = "title")
    private String title;

    @Schema(description = "게시글 내용", example = "detail")
    private String detail;

    @Schema(description = "게시글 작성시간", example = "2023-02-25 11:00:00")
    private String createDate;

    @Schema(description = "런닝 기록 정보")
    @JsonProperty("runningRecord")
    private SimpleRunningRecordDto simpleRunningRecordDto;

    public GetBoardResponse(Board board, int commentCount) {

        this.id = board.getId();
        this.commentCount = commentCount;
        this.simpleMemberDto = new SimpleMemberDto(board.getMember());
        this.title = board.getTitle();
        this.detail = board.getDetail();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.createDate = board.getCreatedDate().format(formatter);

        // board 타입 검사
        if(board instanceof FreeBoard) {
            FreeBoard freeBoard = (FreeBoard) board;
            this.type = "free";
            this.simpleRunningRecordDto = null;
        } else if (board instanceof NoticeBoard) {
            NoticeBoard noticeBoard = (NoticeBoard) board;
            this.type = "notice";
            this.simpleRunningRecordDto = null;
        } else if (board instanceof ReviewBoard) {
            ReviewBoard reviewBoard = (ReviewBoard) board;
            this.simpleRunningRecordDto = new SimpleRunningRecordDto(reviewBoard.getRunningRecord());
        }

    }

}
