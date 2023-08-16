package com.project.runningcrew.board.service;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.repository.FreeBoardRepository;
import com.project.runningcrew.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;

    /**
     * 특정 Crew 의 자유게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     * @param crew 크루 정보
     * @param member 목록을 조회할 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 Crew 의 자유게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     */
    public Slice<SimpleBoardDto> findFreeBoardDtoByCrew(Crew crew, Member member, Pageable pageable) {
        return freeBoardRepository.findFreeBoardDtoByCrew(crew, member, pageable);
    }

}
