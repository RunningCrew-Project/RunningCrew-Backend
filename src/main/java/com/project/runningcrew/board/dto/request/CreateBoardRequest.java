package com.project.runningcrew.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateBoardRequest {

    @Schema(description = "게시글 제목", example = "title")
    @NotBlank(message = "게시글 제목은 필수 값입니다.")
    private String title;

    @Schema(description = "게시글 내용", example = "detail")
    @NotBlank(message = "게시글 내용은 필수 값입니다.")
    private String detail;

    @Schema(description = "게시글 이미지 url 리스트")
    private List<MultipartFile> files = new ArrayList<>();

    @Schema(description = "런닝 기록 아이디", example = "1")
    private Long runningRecordId;
    // nullable : 리뷰 게시글이 아닐 경우, 런닝 기록은 존재하지 않음.

}
