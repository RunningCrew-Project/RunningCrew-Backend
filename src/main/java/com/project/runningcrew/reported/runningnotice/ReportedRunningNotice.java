package com.project.runningcrew.reported.runningnotice;

import com.project.runningcrew.reported.ReportType;
import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "reported_running_notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportedRunningNotice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reported_running_notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_notice_id", nullable = false)
    private RunningNotice runningNotice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    public ReportedRunningNotice(RunningNotice runningNotice, Member member, ReportType reportType) {
        this.runningNotice = runningNotice;
        this.member = member;
        this.reportType = reportType;
    }

    public ReportedRunningNotice(Long id, RunningNotice runningNotice, Member member, ReportType reportType) {
        this.id = id;
        this.runningNotice = runningNotice;
        this.member = member;
        this.reportType = reportType;
    }

}
