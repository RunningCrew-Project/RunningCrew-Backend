package com.project.runningcrew.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SimpleNotificationDto {

    @Schema(description = "공지 id", example = "1")
    private Long id;

    @Schema(description = "공지 생성 시간", example = "2023-03-03 11:11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "공지 제목", example = "새로운 정기런닝이 있습니다.")
    private String  title;

    @Schema(description = "공지 내용", example = "23년 2월 22일 정기런닝")
    private String content;

    @Schema(description = "공지 종류", example = "1")
    private String type;

    @Schema(description = "공지한 크루 이름", example = "1")
    private String crewName;

    @Schema(description = "공지한 크루 이미지", example = "imgUrl")
    private String crewImgUrl;

    @Schema(description = "공지 id", example = "1")
    private Long referenceId;

    public SimpleNotificationDto(Notification notification) {
        this.id = notification.getId();
        this.createdDate = notification.getCreatedDate();
        this.title = notification.getType().getTitle();
        this.content = notification.getContent();
        this.type = notification.getType().toString();
        this.crewName = notification.getCrew().getName();
        this.crewImgUrl = notification.getCrew().getCrewImgUrl();
        this.referenceId = notification.getReferenceId();
    }

}
