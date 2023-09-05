package com.project.runningcrew.board.repository;

import com.project.runningcrew.common.dto.SimpleBoardDto;
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
     * @param member 멤버 정보
     * @param pageable 페이징 정보
     * @return SimpleBoardDto 정보
     * SOFT DELETE 적용
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(b.id, b.createdDate, b.title, u.nickname) " +
            "from Board b " +
            "inner join Member m on b.member = m and m.deleted = false " +
            "inner join User u on m.user = u and u.deleted = false " +
            "where b.member = :member")
    Slice<SimpleBoardDto> findSimpleBoardDtoByMember(@Param("member") Member member, Pageable pageable);


    /**
     * 특정 Member 가 작성한 Board 를 모두 삭제한다.
     * @param member 멤저 정보
     * SOFT DELETE 적용
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Board b " +
            "set b.deleted = true " +
            "where b.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * 특정 crew 의 모든 Board 를 삭제한다.
     * @param crew 크루 정보
     * SOFT DELETE 적용
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Board b " +
            "set b.deleted = true " +
            "where b in ( " +
            "select b2 " +
            "from Board b2 " +
            "where b2.member.crew = :crew )")
    void deleteAllByCrew(@Param("crew") Crew crew);

    @Query("select b.id from Board b " +
            "inner join b.member m on m.deleted = false " +
            "where m.crew = :crew")
    List<Long> findIdsByCrew(@Param("crew") Crew crew);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Board b set b.deleted = true where b.id in :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);

}
