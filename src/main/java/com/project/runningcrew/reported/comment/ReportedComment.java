package com.project.runningcrew.reported.comment;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.reported.ReportType;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update reported_comments set deleted = true where reported_comment_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "reported_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportedComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reported_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column
    private boolean deleted = false;

    public ReportedComment(Crew crew, Comment comment, Member member, ReportType reportType) {
        this.crew = crew;
        this.comment = comment;
        this.member = member;
        this.reportType = reportType;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
