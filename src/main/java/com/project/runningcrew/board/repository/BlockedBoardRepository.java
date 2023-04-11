package com.project.runningcrew.board.repository;

import com.project.runningcrew.blocked.BlockedBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedBoardRepository extends JpaRepository<BlockedBoard, Long> {

}
