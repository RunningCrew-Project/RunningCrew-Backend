package com.project.runningcrew.reported.totalpost.runningnotice;

import com.project.runningcrew.reported.ReportType;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.reported.totalpost.ReportedTotalPost;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update reported_total_posts set deleted = true where reported_total_post_id = ?")
@Getter
@DiscriminatorValue("running_notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportedRunningNotice extends ReportedTotalPost {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_notice_id")
    private RunningNotice runningNotice;

    public ReportedRunningNotice(RunningNotice runningNotice, Member member, ReportType reportType) {
        super(member, reportType);
        this.runningNotice = runningNotice;
    }

    public ReportedRunningNotice(Long id, RunningNotice runningNotice, Member member, ReportType reportType) {
        super(id, member, reportType);
        this.runningNotice = runningNotice;
    }

}
