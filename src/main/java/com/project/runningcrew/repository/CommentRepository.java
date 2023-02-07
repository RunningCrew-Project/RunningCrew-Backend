package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Board 의 모든 Comment 를 가져오는 메소드다.
     * @param boardId
     * @return List of Comment
     */
    @Query("select c from Comment c where c.board.id = :boardId")
    List<Comment> findAllByBoardId(@Param("boardId") Long boardId);


    /**
     * 특정 Member 의 모든 Comment 를 가져오는 메소드다.
     * @param memberId
     * @return List of Comment
     */
    @Query("select c from Comment c where c.member.id = :memberId")
    List<Comment> findAllByMemberId(@Param("memberId") Long memberId);



}
