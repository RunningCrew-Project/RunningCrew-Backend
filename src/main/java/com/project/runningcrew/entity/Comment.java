package com.project.runningcrew.entity;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.members.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotBlank(message = "댓글 내용은 필수값입니다.")
    @Size(min = 1, max = 200, message = "댓글 내용은 1 자 이상 200 자 이하입니다.")
    @Column(nullable = false, length = 200)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public Comment(Member member, String detail, Board board) {
        this.member = member;
        this.detail = detail;
        this.board = board;
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

}
