package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.boards.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * @param memberId
     * @return list of Board
     * 특정 member 가 작성한 모든 게시물을 반환한다.
     */
    @Query("select b from Board b where b.member.id = :memberId")
    List<Board> findAllByMemberId(@Param("memberId") Long memberId);


    /**
     * @param search
     * @return list of Board
     * 특정 단어 search 를 제목 or 내용에 포함하는 게시물을 모두 반환한다.
     */
    @Query("select b from Board b where b.title like %:search% or b.detail like %:search%")
    List<Board> findAllByTitleAndDetail(@Param("search") String search);



}
