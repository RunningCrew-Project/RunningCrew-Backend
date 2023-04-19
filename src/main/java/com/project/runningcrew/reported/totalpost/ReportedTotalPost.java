package com.project.runningcrew.reported.totalpost;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.reported.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
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

    public ReportedTotalPost(Member member, ReportType reportType) {
        this.member = member;
        this.reportType = reportType;
    }
    public ReportedTotalPost(Long id, Member member, ReportType reportType) {
        this.id = id;
        this.member = member;
        this.reportType = reportType;
    }


}
