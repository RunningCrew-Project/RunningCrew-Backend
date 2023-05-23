package com.project.runningcrew.exception.notFound;

public class BlockedInfoNotFoundException extends ResourceNotFoundException{
    public BlockedInfoNotFoundException() {
        super(ResourceNotFoundErrorCode.BLOCKED_INFO_NOT_FOUND);
    }
}
