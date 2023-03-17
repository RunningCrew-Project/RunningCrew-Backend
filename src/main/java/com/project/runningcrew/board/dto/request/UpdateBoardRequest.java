package com.project.runningcrew.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateBoardRequest {

    @Schema(description = "수정 게시글 제목", example = "new_title")
    @NotBlank(message = "게시글 제목은 필수 값입니다.")
    @Size(min = 1, max = 50, message = "게시글 제목은 1자 이상 50자 이하입니다.")
    private String title;

    @Schema(description = "수정 게시글 내용", example = "new_detail")
    @NotBlank(message = "게시글 내용은 필수 값입니다.")
    @Size(min = 1, max = 1000, message = "게시글 내용은 1자 이상 1000자 이하입니다.")
    private String detail;

    @Schema(description = "추가 이미지 파일 url 리스트 정보")
    private List<MultipartFile> addFiles = new ArrayList<>();

    @Schema(description = "삭제 이미지 아아디 리스트정보")
    private List<Long> deleteFiles = new ArrayList<>();

    @Schema(description = "수정 런닝 기록 아이디")
    @Positive(message = "런닝 기록 아이디는 1 이상의 수입니다.")
    private Integer runningRecordId;


}
