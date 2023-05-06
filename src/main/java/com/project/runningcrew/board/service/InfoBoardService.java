package com.project.runningcrew.board.service;

import com.project.runningcrew.board.entity.InfoBoard;
import com.project.runningcrew.board.repository.InfoBoardRepository;
import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InfoBoardService {

    private final InfoBoardRepository infoBoardRepository;

    /**
     * 미사용 예정!!
     * 미사용 예정!!
     * 미사용 예정!!
     */
    public Slice<InfoBoard> findInfoBoardByCrew(Crew crew, Pageable pageable) {
        return infoBoardRepository.findInfoBoardByCrew(crew, pageable);
    }


    /**
     * 특정 Crew 의 정보게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     * @param crew 크루 정보
     * @param member 정보를 조회하는 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 Crew 의 정보게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     */
    public Slice<SimpleBoardDto> findInfoBoardDtoByCrew(Crew crew, Member member, Pageable pageable) {
        return infoBoardRepository.findInfoBoardDtoByCrew(crew, member, pageable);
    }


}
