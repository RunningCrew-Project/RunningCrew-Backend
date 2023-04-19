package com.project.runningcrew.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class SimpleBoardDto {

    private final Long id;

    private final LocalDateTime createdDate;

    private final String title;

    private final String nickname;

}
