package com.project.runningcrew.board.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@SQLDelete(sql = "update boards set deleted = true where board_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "boards")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "board_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column
    private boolean deleted = false;

    public Board(Member member, String title, String detail) {
        this.member = member;
        this.title = title;
        this.detail = detail;
    }

    public Board(Long id, Member member, String title, String detail) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.detail = detail;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

}
