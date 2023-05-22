package com.project.runningcrew.blocked.entity;

import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "blocked_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocked_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member blockerMember;


    @Column(name = "blocked_member_id")
    private Long blockedMemberId;


    public BlockedInfo(Member blockerMember, Long blockedMemberId) {
        this.blockerMember = blockerMember;
        this.blockedMemberId = blockedMemberId;
    }

}
