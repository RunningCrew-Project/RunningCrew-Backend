package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.comment.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findAllByBoard(Board board);

}
