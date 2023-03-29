package com.project.runningcrew.runningnotice.entity;

public enum NoticeType {
    REGULAR("정기런닝"),
    INSTANT("번개런닝");

    private String name;

    NoticeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
