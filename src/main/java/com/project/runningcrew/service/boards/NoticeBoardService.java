package com.project.runningcrew.service.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.boards.NoticeBoardRepository;
import com.project.runningcrew.service.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;
    private final CrewRepository crewRepository;

    /**
     * 입력받은 Crew 의 공지게시판 목록을 모두 보여준다.
     * @param crew 입력받은 Crew
     * @param pageable
     * @return 입력받은 Crew 의 공지게시판 목록(페이징 적용)
     */
    public Slice<NoticeBoard> findNoticeBoardByCrew(Crew crew, Pageable pageable) {
        return noticeBoardRepository.findNoticeBoardByCrew(crew, pageable);
    }


}
