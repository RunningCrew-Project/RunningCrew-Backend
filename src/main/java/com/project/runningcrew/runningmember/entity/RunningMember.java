package com.project.runningcrew.runningmember.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update running_members set deleted = true where running_member_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "running_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunningMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "running_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "running_notice_id", nullable = false)
    private RunningNotice runningNotice;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column
    private boolean deleted = false;

    public RunningMember(RunningNotice runningNotice, Member member) {
        this.runningNotice = runningNotice;
        this.member = member;
    }

    public RunningMember(Long id, RunningNotice runningNotice, Member member) {
        this.id = id;
        this.runningNotice = runningNotice;
        this.member = member;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
