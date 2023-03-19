package com.project.runningcrew.runningnotice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class UpdateRunningNoticeRequest {

    @NotBlank(message = "런닝 공지글 제목은 필수값입니다.")
    @Size(min = 1, max = 50, message = "런닝 공지글 제목은 1 자 이상 50 자 이하입니다.")
    @Schema(description = "런닝 공지 제목", example = "title")
    private String title;

    @NotBlank(message = "런닝 공지글 내용은 필수값입니다.")
    @Size(min = 1, max = 1000, message = "런닝 공지글 내용은 1 자 이상 1000 자 이하입니다.")
    @Schema(description = "런닝 공지 내용", example = "detail")
    private String detail;

    @NotNull
    @Schema(description = "런닝 시작 일자", example = "2023-03-03 11:11:11")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime runningDateTime;

    @Schema(description = "첨부한 이전 런닝 기록의 id")
    private Long preRunningRecordId;

    @Schema(description = "추가할 이미지 파일들")
    private List<MultipartFile> addFiles;

    @Schema(description = "삭제할 이미지들의 id 가 담긴 리스트")
    private List<Long> deleteFiles;

    public List<MultipartFile> getAddFiles() {
        return Objects.requireNonNullElse(addFiles, Collections.emptyList());
    }

    public List<Long> getDeleteFiles() {
        return Objects.requireNonNullElse(deleteFiles, Collections.emptyList());
    }

}
