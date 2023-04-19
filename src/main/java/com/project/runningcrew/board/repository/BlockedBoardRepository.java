package com.project.runningcrew.board.repository;

import com.project.runningcrew.reported.totalpost.board.ReportedBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedBoardRepository extends JpaRepository<ReportedBoard, Long> {

}
