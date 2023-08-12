package com.project.runningcrew.blocked.repository;

import com.project.runningcrew.blocked.entity.BlockedInfo;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockedInfoRepository extends JpaRepository<BlockedInfo, Long> {


    /**
     * 입력받은 Member 가 차단한 Blocked Member List 를 반환한다.
     * @param member Blocker Member
     * @return Blocked Member List
     *
     * SOFT DELETE 적용
     */
    @Query("select bi.blockedMemberId " +
            "from BlockedInfo bi " +
            "inner join Member m on bi.blockerMember = m and m.deleted = false " +
            "where bi.blockerMember = :member")
    List<Long> findBlockedListByMember(@Param("member") Member member);


    /**
     * Blocker Member 의 Blocked Member 차단 여부를 반환한다.
     * @param blockerId 차단을 실행한 Member 의 아이디 정보
     * @param blockedId 차단을 당한 Member 의 아이디 정보
     * @return Blocker Member 의 Blocked Member 차단 여부
     */
    @Query("select case when count(bi) > 0 then true else false end " +
            "from BlockedInfo bi " +
            "where bi.blockerMember.id = :blockerId " +
            "and bi.blockedMemberId = :blockedId")
    boolean isBlocked(@Param("blockerId") Long blockerId, @Param("blockedId") Long blockedId);


    /**
     * 차단 정보와 관련된 두 Member 의 아이디 값으로 BlockedInfo 를 찾아 반환한다.
     * @param blockerId 차단을 실행한 Member 의 아이디 정보
     * @param blockedId 차단을 당한 Member 의 아이디 정보
     * @return BlockedInfo
     */
    @Query("select bi " +
            "from BlockedInfo bi " +
            "where bi.blockerMember.id = :blockerId " +
            "and bi.blockedMemberId = :blockedId")
    BlockedInfo findByTwoMemberId(@Param("blockerId") Long blockerId, @Param("blockedId") Long blockedId);


    /**
     * Blocker Member 가 차단한 멤버 목록 전체를 삭제한다.
     * @param member Blocker Member 정보
     *
     * SOFT DELETE 적용
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update BlockedInfo bi " +
            "set bi.deleted = true " +
            "where bi.blockerMember = :member")
    void deleteAllByBlockerMember(@Param("member") Member member);

}
