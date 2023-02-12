package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Comment;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.members.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Board 의 모든 Comment 를 가져오는 메소드다.
     * @param board
     * @return List of Comment
     */

    List<Comment> findAllByBoard(Board board);


    /**
     * 특정 Member 의 모든 Comment 를 가져오는 메소드다.
     * @param member
     * @return List of Comment
     */
    List<Comment> findAllByMember(Member member);



}
