package com.project.runningcrew.comment.dto.response;

import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetCommentUserDto {

    @Schema(description = "작성자 계정 아이디", example = "1")
    private Long id;

    @Schema(description = "작성자 계정 이미지", example = "imgUrl")
    private String imgUrl;

    @Schema(description = "작성자 계정 닉네임", example = "nickname")
    private String nickname;

    public GetCommentUserDto(User user) {
        this.id = user.getId();
        this.imgUrl = user.getImgUrl();
        this.nickname = user.getNickname();
    }

}
