package com.project.runningcrew.entity.comment;

import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue("runningNotice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunningNoticeComment extends Comment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_notice_id")
    private RunningNotice runningNotice;

    public RunningNoticeComment(Member member, String detail, RunningNotice runningNotice) {
        super(member, detail);
        this.runningNotice = runningNotice;
    }

    public RunningNoticeComment(Long id, Member member, String detail, RunningNotice runningNotice) {
        super(id, member, detail);
        this.runningNotice = runningNotice;
    }

}
