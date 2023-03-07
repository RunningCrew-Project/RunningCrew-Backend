package com.project.runningcrew.comment.repository;

import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Member 의 모든 Comment 를 가져온다. (페이징 적용)
     * @param member
     * @param pageable
     * @return slice of Comment
     */
    Slice<Comment> findAllByMember(Member member, Pageable pageable);



    // board 에 작성된 모든 comment 삭제하기 (쿼리 하나)

    // RunningNotice 에 작성된 모든 comment 삭제하기 (쿼리 하나)

}
