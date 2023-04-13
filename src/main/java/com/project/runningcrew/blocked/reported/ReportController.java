package com.project.runningcrew.blocked.reported;

import com.project.runningcrew.blocked.reported.board.dto.GetReportedBoardResponse;
import com.project.runningcrew.blocked.reported.comment.dto.GetReportedCommentResponse;
import com.project.runningcrew.common.dto.PagingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReportController {

    @PostMapping(value = "")
    public ResponseEntity<Void> reportBoard() {
        return null;
    }

    @PostMapping(value = "")
    public ResponseEntity<Void> reportComment() {
        return null;
    }


    @GetMapping(value = "")
    public ResponseEntity<PagingResponse<GetReportedBoardResponse>> getReportedBoards() {
        return null;
    }

    @GetMapping(value = "")
    public ResponseEntity<PagingResponse<GetReportedCommentResponse>> getReportedComments() {
        return null;
    }


}
