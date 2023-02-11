package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.boards.BlockedBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedBoardRepository extends JpaRepository<BlockedBoard, Long> {

}
