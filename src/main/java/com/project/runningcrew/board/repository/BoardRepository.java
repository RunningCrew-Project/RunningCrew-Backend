package com.project.runningcrew.board.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
     * 특정 keyword 를 제목 or 내용에 포함하는 게시물을 모두 반환한다. - 페이징 적용
     * @param keyword
     * @return slice of Board
     */
    @Query("select b from Board b where b.member.crew = :crew and (b.title like %:keyword% or b.detail like %:keyword%)")
    Slice<Board> findSliceAllByCrewAndKeyWord(@Param("keyword") String keyword, @Param("crew") Crew crew, Pageable pageable);


    /**
     * 특정 Member 가 작성한 Board 를 모두 삭제한다.
     * @param member
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Board b where b.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * 특정 crew 의 모든 Board 를 삭제한다.
     * @param crew
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Board b where b in (select b2 from Board b2 where b2.member.crew = :crew)")
    void deleteAllByCrew(@Param("crew") Crew crew);


}
