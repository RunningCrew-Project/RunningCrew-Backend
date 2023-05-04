package com.project.runningcrew.blocked.service;

import com.project.runningcrew.blocked.entity.BlockedInfo;
import com.project.runningcrew.blocked.repository.BlockedInfoRepository;
import com.project.runningcrew.exception.notFound.BlockedInfoNotFoundException;
import com.project.runningcrew.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class BlockedInfoService {

    private final BlockedInfoRepository blockedInfoRepository;

    /**
     * 입력받은 Member 가 차단한 Blocked Member List 를 반환한다.
     * @param member Member 정보
     * @return Blocked Member List
     */
    public List<Long> findBlockedListByMember(Member member) {
        return blockedInfoRepository.findBlockedListByMember(member);
    }


    /**
     * Blocker Member 의 Blocked Member 차단 여부를 반환한다.
     * @param blockerId 차단을 실행한 Member 의 아이디 정보
     * @param blockedId 차단을 당한 Member 의 아이디 정보
     * @return 차단한 경우 true , 차단하지 않은 경우 false
     */
    public boolean isBlocked(Long blockerId, Long blockedId) {
        return blockedInfoRepository.isBlocked(blockerId, blockedId);
    }


    /**
     * 차단 정보와 관련된 두 Member 의 아이디 값으로 BlockedInfo 를 찾아 반환한다.
     * @param blockerId 차단을 실행한 Member 의 아이디 정보
     * @param blockedId 차단을 당한 Member 의 아이디 정보
     * @return BlockedInfo
     * @throws BlockedInfoNotFoundException 차단 정보가 없는데 조회를 시도한 경우
     */
    public BlockedInfo findByTwoMemberId(Long blockerId, Long blockedId) {
        if(!blockedInfoRepository.isBlocked(blockerId, blockedId)) {
            throw new BlockedInfoNotFoundException();
        }
        return blockedInfoRepository.findByTwoMemberId(blockerId, blockedId);
    }


    public BlockedInfo findById(Long id) {
        return blockedInfoRepository.findById(id).orElseThrow(BlockedInfoNotFoundException::new);
    }


    public Long saveBlockedInfo(BlockedInfo blockedInfo) {
        return blockedInfoRepository.save(blockedInfo).getId();
    }



    public void deleteBlockedInfo(BlockedInfo blockedInfo) {
        blockedInfoRepository.delete(blockedInfo);
    }

}
