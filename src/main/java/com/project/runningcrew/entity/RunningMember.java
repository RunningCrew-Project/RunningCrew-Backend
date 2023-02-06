package com.project.runningcrew.entity;

import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunningMember extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "running_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "running_notice_id", nullable = false)
    private RunningNotice runningNotice;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public RunningMember(RunningNotice runningNotice, Member member) {
        this.runningNotice = runningNotice;
        this.member = member;
    }

}
