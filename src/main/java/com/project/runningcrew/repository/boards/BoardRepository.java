package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.members.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * @param member
     * @return list of Board
     * 특정 member 가 작성한 모든 게시물을 반환한다.
     */
    List<Board> findAllByMember(Member member);


    /**
     * @param keyword
     * @return list of Board
     * 특정 keyword 를 제목 or 내용에 포함하는 게시물을 모두 반환한다.
     */
    @Query("select b from Board b where b.title like %:keyword% or b.detail like %:keyword%")
    List<Board> findAllByTitleAndDetail(@Param("keyword") String keyword);





}
