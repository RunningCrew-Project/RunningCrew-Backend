package com.project.runningcrew.reported.totalpost;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.reported.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update reported_total_posts set deleted = true where reported_total_post_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "reported_total_posts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ReportedTotalPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reported_total_post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column
    private boolean deleted = false;

    public ReportedTotalPost(Member member, ReportType reportType) {
        this.member = member;
        this.reportType = reportType;
    }
    public ReportedTotalPost(Long id, Member member, ReportType reportType) {
        this.id = id;
        this.member = member;
        this.reportType = reportType;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
