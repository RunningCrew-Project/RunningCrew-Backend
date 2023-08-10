package com.project.runningcrew.comment.entity;

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
@SQLDelete(sql = "update comments set deleted = true where comment_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "comments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "comment_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotBlank(message = "댓글 내용은 필수값입니다.")
    @Size(min = 1, max = 200, message = "댓글 내용은 1 자 이상 200 자 이하입니다.")
    @Column(nullable = false, length = 200)
    private String detail;

    @Column
    private boolean deleted = false;

    public Comment(Member member, String detail) {
        this.member = member;
        this.detail = detail;
    }

    public Comment(Long id, Member member, String detail) {
        this.id = id;
        this.member = member;
        this.detail = detail;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

}
