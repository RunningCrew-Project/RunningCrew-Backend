package com.project.runningcrew.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateNotReviewBoardRequest {

    @Schema(description = "게시글 제목", example = "title")
    @NotBlank(message = "게시글 제목은 필수 값입니다.")
    @Size(min = 1, max = 50, message = "게시글 제목은 1자 이상 50자 이하입니다.")
    private String title;

    @Schema(description = "게시글 내용", example = "detail")
    @NotBlank(message = "게시글 내용은 필수 값입니다.")
    @Size(min = 1, max = 1000, message = "게시글 내용은 1자 이상 1000자 이하입니다.")
    private String detail;

    @Schema(description = "게시글 이미지 url 리스트")
    private List<MultipartFile> files = new ArrayList<>();



}
