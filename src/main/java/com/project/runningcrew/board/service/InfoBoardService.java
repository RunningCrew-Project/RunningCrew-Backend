package com.project.runningcrew.board.service;

import com.project.runningcrew.board.entity.InfoBoard;
import com.project.runningcrew.board.repository.InfoBoardRepository;
import com.project.runningcrew.crew.entity.Crew;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InfoBoardService {

    private final InfoBoardRepository infoBoardRepository;

    /**
     * 입력받은 Crew 의 정보게시판 목록을 모두 보여준다.
     * @param crew
     * @param pageable
     * @return 입력받은 Crew 의 정보게시판 목록(페이징 적용)
     */
    public Slice<InfoBoard> findInfoBoardByCrew(Crew crew, Pageable pageable) {
        return infoBoardRepository.findInfoBoardByCrew(crew, pageable);
    }

}
