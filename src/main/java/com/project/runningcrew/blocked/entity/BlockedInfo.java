package com.project.runningcrew.blocked.entity;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update blocked_info set deleted = true where blocked_info_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "blocked_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocked_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member blockerMember;

    @Column(name = "blocked_member_id")
    private Long blockedMemberId;

    @Column
    private boolean deleted = false;

    public BlockedInfo(Crew crew, Member blockerMember, Long blockedMemberId) {
        this.crew = crew;
        this.blockerMember = blockerMember;
        this.blockedMemberId = blockedMemberId;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
