package com.project.runningcrew.board.service;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.board.repository.ReviewBoardRepository;
import com.project.runningcrew.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewBoardService {

    private final ReviewBoardRepository reviewBoardRepository;

    /**
     * 미사용 예정!!
     * 미사용 예정!!
     * 미사용 예정!!
     */
    public Slice<ReviewBoard> findReviewBoardByCrew(Crew crew, Pageable pageable) {
        return reviewBoardRepository.findReviewBoardByCrew(crew, pageable);
    }

    /**
     * 특정 Crew 의 리뷰게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     * @param crew 크루 정보
     * @param member 정보를 조회하는 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 Crew 의 리뷰게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     */
    public Slice<SimpleBoardDto> findReviewBoardDtoByCrew(Crew crew, Member member, Pageable pageable) {
        return reviewBoardRepository.findReviewBoardDtoByCrew(crew, member, pageable);
    }

}
