package com.project.runningcrew.service.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.boards.FreeBoardRepository;
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
    private final CrewRepository crewRepository;

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
