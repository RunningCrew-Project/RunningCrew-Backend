package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.boards.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    /**
     * @param pageable
     * @return page of FreeBoard
     * 자유게시판 전체 게시물 출력에 paging 을 적용한다.
     */
    @Query("select fb from FreeBoard fb")
    Slice<FreeBoard> findFreeBoardAll(Pageable pageable);

}
