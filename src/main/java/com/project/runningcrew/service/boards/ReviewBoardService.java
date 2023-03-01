package com.project.runningcrew.service.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.ReviewBoard;
import com.project.runningcrew.repository.boards.ReviewBoardRepository;
import lombok.RequiredArgsConstructor;
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
     * 입력받은 Crew 의 리뷰게시판 목록을 모두 보여준다.
     * @param crew 입력받은 Crew
     * @param pageable
     * @return 입력받은 Crew 의 리뷰게시판 목록(페이징 적용)
     */
    public Slice<ReviewBoard> findReviewBoardByCrew(Crew crew, Pageable pageable) {
        return reviewBoardRepository.findReviewBoardByCrew(crew, pageable);
    }

}
