package com.project.runningcrew.runningnotice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NoticeWithUserDto {

    private final Long id;

    private final LocalDateTime createdDate;

    private final String title;

    private final String nickname;

}
