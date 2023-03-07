package com.project.runningcrew.board.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.repository.FreeBoardRepository;
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
     * 입력받은 Crew 의 자유게시판 목록을 모두 보여준다.
     * @param crew 입력받은 Crew
     * @param pageable
     * @return 입력받은 Crew 의 자유게시판 목록(페이징 적용)
     */
    public Slice<FreeBoard> findFreeBoardByCrew(Crew crew, Pageable pageable) {
        return freeBoardRepository.findFreeBoardByCrew(crew, pageable);
    }

}
