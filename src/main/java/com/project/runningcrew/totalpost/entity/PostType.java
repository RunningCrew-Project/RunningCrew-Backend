package com.project.runningcrew.totalpost.entity;

import com.project.runningcrew.exception.notFound.PostTypeNotFoundException;

import java.util.Arrays;

public enum PostType {

    BOARD("board"),
    RUNNING_NOTICE("runningNotice");

    private String value;

    PostType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PostType getPostType(String value) {
        return Arrays.stream(PostType.values())
                .filter(p -> value.equals(p.getValue()))
                .findAny()
                .orElseThrow(PostTypeNotFoundException::new);
    }

}
