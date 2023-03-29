package com.project.runningcrew.totalpost.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TotalPost {

    private Long id;
    private LocalDateTime createdDate;
    private String title;
    private String nickname;
    private PostType postType;

}
