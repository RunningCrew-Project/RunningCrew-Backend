package com.project.runningcrew.exception.notFound;

public class BlockedInfoNotFoundException extends ResourceNotFoundException{
    public BlockedInfoNotFoundException() {
        super("존재하지 않는 차단 정보입니다.");
    }
}
