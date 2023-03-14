package com.project.runningcrew.exception.badinput;

import com.project.runningcrew.member.entity.MemberRole;

import java.util.Map;

public class UpdateMemberRoleException extends BadInputException {

    public UpdateMemberRoleException(MemberRole memberRole) {
        super("수정할 수 없는 멤버의 권한을 가지고 있습니다. ", Map.of("role", memberRole.toString()));
    }
}
