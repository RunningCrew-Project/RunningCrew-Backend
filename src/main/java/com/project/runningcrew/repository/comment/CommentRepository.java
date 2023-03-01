package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.comment.Comment;
import com.project.runningcrew.entity.members.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    /**
     * 특정 Member 의 모든 Comment 를 가져오는 메소드다.
     * @param member
     * @return List of Comment
     */
    List<Comment> findAllByMember(Member member);


    /**
     * 특정 Member 의 모든 Comment 를 가져온다. (페이징 적용)
     * @param member
     * @param pageable
     * @return slice of Comment
     */
    Slice<Comment> findAllByMember(Member member, Pageable pageable);
}
