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

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Member 의 모든 Comment 를 가져온다. (페이징 적용)
     * @param member 멤버 정보
     * @param pageable 페이징 정보
     * @return slice of Comment
     */
    @Query("select c " +
            "from Comment c " +
            "inner join Member m on c.member = m and m.deleted = false " +
            "where c.member = :member")
    Slice<Comment> findAllByMember(Member member, Pageable pageable);

    /**
     * 해당 member 가 작성한 모든 comment 를 삭제한다.
     * @param member 멤버 정보
     * SOFT DELETE 적용
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Comment c " +
            "set c.deleted = true " +
            "where c.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * 해당 crew 의 모든 comment 를 삭제한다.
     * @param crew 크루 정보
     * SOFT DELETE 적용
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Comment c " +
            "set c.deleted = true " +
            "where c in ( " +
            "select c2 from Comment c2 " +
            "where c.member.crew = :crew )")
    void deleteAllByCrew(@Param("crew") Crew crew);

    @Query("select c.id from Comment c " +
            "inner join c.member m on m.deleted = false " +
            "where m.crew = :crew")
    List<Long> findIdsByCrew(@Param("crew") Crew crew);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Comment c set c.deleted = true where c.id in :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);

}
