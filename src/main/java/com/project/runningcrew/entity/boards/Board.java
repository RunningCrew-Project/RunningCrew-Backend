package com.project.runningcrew.entity.boards;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.members.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "board_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotBlank(message = "게시글 제목은 필수값입니다.")
    @Size(min = 1, max = 50, message = "게시글 제목은 1 자 이상 50 자 이하입니다.")
    @Column(nullable = false, length = 50)
    private String title;

    @NotBlank(message = "게시글 내용은 필수값입니다.")
    @Size(min = 1, max = 1000, message = "게시글 내용은 1 자 이상 1000 자 이하입니다.")
    @Column(nullable = false, length = 1000)
    private String detail;

    public Board(Member member, String title, String detail) {
        this.member = member;
        this.title = title;
        this.detail = detail;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

}
