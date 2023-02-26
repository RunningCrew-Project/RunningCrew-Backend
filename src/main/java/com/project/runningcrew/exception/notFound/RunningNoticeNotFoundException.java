package com.project.runningcrew.exception.notFound;

public class RunningNoticeNotFoundException extends ResourceNotFoundException {
    public RunningNoticeNotFoundException() {
        super("존재하지 않는 런닝공지입니다.");
    }
}
