package com.project.runningcrew.exception.notFound;

public class SidoAreaNotFoundException extends ResourceNotFoundException{
    public SidoAreaNotFoundException() {
        super("존재하지 않는 시/도입니다.");
    }
}
