package com.project.runningcrew.exception.notFound;

public class BoardNotFoundException extends ResourceNotFoundException{
    public BoardNotFoundException() {
        super(ResourceNotFoundErrorCode.BOARD_NOT_FOUND);
    }
}
