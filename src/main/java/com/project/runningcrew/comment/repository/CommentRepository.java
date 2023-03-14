package com.project.runningcrew.comment.repository;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Member 의 모든 Comment 를 가져온다. (페이징 적용)
     * @param member
     * @param pageable
     * @return slice of Comment
     */
    Slice<Comment> findAllByMember(Member member, Pageable pageable);

    /**
     * 해당 member 가 작성한 모든 comment 를 삭제한다.
     * @param member
     */
    @Modifying
    @Query("delete from Comment c where c.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * 해당 crew 의 모든 comment 를 삭제한다.
     * @param crew
     */
    @Modifying
    @Query("delete from Comment c where c.member.crew = :crew")
    void deleteAllByCrew(@Param("crew") Crew crew);



}
