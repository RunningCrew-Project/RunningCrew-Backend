package com.project.runningcrew.exception.badinput;

import com.project.runningcrew.member.entity.MemberRole;

import java.util.Map;

public class UpdateMemberRoleException extends BadInputException {

    public UpdateMemberRoleException(MemberRole memberRole) {
        super(BadInputErrorCode.UPDATE_MEMBER_ROLE, Map.of("role", memberRole.toString()));
    }
}
