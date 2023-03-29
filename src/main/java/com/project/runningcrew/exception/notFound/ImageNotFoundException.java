package com.project.runningcrew.exception.notFound;

public class ImageNotFoundException extends ResourceNotFoundException{
    public ImageNotFoundException() {
        super("존재하지 않는 이미지입니다.");
    }
}
