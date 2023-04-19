package com.project.runningcrew.exception.notFound;

public class ReportTypeNotFoundException extends ResourceNotFoundException{
    public ReportTypeNotFoundException() {
        super("존재하지 않는 글 종류입니다.");
    }
}
