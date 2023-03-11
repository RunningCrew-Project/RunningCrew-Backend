package com.project.runningcrew.crew.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CrewInfoRequest {

    @Schema(description = "크루 이름", example = "crew")
    @NotBlank(message = "크루 이름은 필수값입니다.")
    @Size(min = 1, max = 100, message = "크루 이름은 1 자 이상 100 자 이하입니다.")
    private String name;

    @Schema(description = "크루 소개", example = "introduction")
    @NotBlank(message = "크루 소개는 필수값입니다.")
    @Size(min = 1, max = 500, message = "크루 소개는 1 자 이상 500 자 이하입니다.")
    private String introduction;

    @Schema(description = "크루 활동 동 id", example = "1")
    @Positive(message = "동 id 는 1 이상의 수입니다.")
    private Long dongId;

    @Schema(description = "크루 이미지")
    @NotNull(message = "이미지는 필수값입니다.")
    private MultipartFile file;

}
