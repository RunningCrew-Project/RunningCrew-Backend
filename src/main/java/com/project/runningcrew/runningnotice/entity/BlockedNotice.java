package com.project.runningcrew.runningnotice.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "blocked_notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedNotice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocked_notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_notice_id", nullable = false)
    private RunningNotice runningNotice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public BlockedNotice(RunningNotice runningNotice, Member member) {
        this.runningNotice = runningNotice;
        this.member = member;
    }

    public BlockedNotice(Long id, RunningNotice runningNotice, Member member) {
        this.id = id;
        this.runningNotice = runningNotice;
        this.member = member;
    }

}
