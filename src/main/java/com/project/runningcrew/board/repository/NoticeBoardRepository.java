package com.project.runningcrew.board.repository;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.NoticeBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {

    /**
     * 공지게시판 전체 게시물 출력에 paging 적용
     * @param pageable
     * @return slice of NoticeBoard
     */
    @Query("select nb from NoticeBoard nb where nb.member.crew = :crew")
    Slice<NoticeBoard> findNoticeBoardByCrew(@Param("crew") Crew crew, Pageable pageable);

    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(nb.id, nb.createdDate, nb.title, u.nickname) " +
            "from NoticeBoard nb " +
            "inner join Member m on m.crew = :crew " +
            "inner join User u on m.user = u")
    Slice<SimpleBoardDto> findNoticeBoardDtoByCrew(@Param("crew") Crew crew, Pageable pageable);
}
