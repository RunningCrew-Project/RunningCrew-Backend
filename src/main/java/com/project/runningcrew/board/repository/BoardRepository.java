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
     * 특정 member 가 작성한 모든 게시물을 반환한다.(Dto 매핑)
     * @param member 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 member 가 작성한 모든 게시물을 반환한다.(Dto 매핑)
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(b.id, b.createdDate, b.title, u.nickname) " +
            "from Board b " +
            "inner join Member m on b.member = :member " +
            "inner join User u on m.user = u " +
            "where b.member = m")
    Slice<SimpleBoardDto> findSimpleBoardDtoByMember(@Param("member") Member member, Pageable pageable);



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




    /**
     * 미사용 예정!!
     * 미사용 예정!!
     * 미사용 예정!!
     */
    Slice<Board> findByMember(Member member, Pageable pageable);


}
