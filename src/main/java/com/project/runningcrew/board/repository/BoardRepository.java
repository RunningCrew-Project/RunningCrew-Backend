package com.project.runningcrew.board.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * 특정 member 가 작성한 모든 게시물을 반환한다.
     * @param member
     * @return list of Board
     */
    Slice<Board> findByMember(Member member, Pageable pageable);


    /**
     * 특정 keyword 를 제목 or 내용에 포함하는 게시물을 모두 반환한다.
     * @param keyword
     * @return list of Board
     */
    @Query("select b from Board b where b.member.crew = :crew and (b.title like %:keyword% or b.detail like %:keyword%)")
    List<Board> findListAllByCrewAndKeyWord(@Param("keyword") String keyword, @Param("crew") Crew crew);


    /**
     * @param keyword
     * @return slice of Board
     * 특정 keyword 를 제목 or 내용에 포함하는 게시물을 모두 반환한다.(paging 적용)
     */
    @Query("select b from Board b where b.member.crew = :crew and (b.title like %:keyword% or b.detail like %:keyword%)")
    Slice<Board> findSliceAllByCrewAndKeyWord(@Param("keyword") String keyword, @Param("crew") Crew crew);


}
