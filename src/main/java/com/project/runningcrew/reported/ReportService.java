package com.project.runningcrew.reported;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.reported.board.ReportedBoard;
import com.project.runningcrew.reported.board.ReportedBoardRepository;
import com.project.runningcrew.reported.comment.ReportedComment;
import com.project.runningcrew.reported.comment.ReportedCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {


    /**
     * 한 번 신고당한 게시글 or 댓글은 다시 신고당하지 않는다 : 신고자가 많을 수 있기 때문에 아닌거같음, 디자인 상 신고자를 표기하지 않음(익명성)
     */

    private final ReportedBoardRepository reportedBoardRepository;
    private final ReportedCommentRepository reportedCommentRepository;


    /**
     * 입력받은 게시글 신고 정보를 저장한다.
     * @param reportedBoard 게시글 신고 정보
     * @return 게시글 신고 정보의 아이디 값
     */
    public Long saveReportedBoard(ReportedBoard reportedBoard) {
        ReportedBoard savedBoard = reportedBoardRepository.save(reportedBoard);
        return savedBoard.getId();
    }

    /**
     * 입력받은 댓글 신고 정보를 저장한다.
     * @param reportedComment 댓글 신고 정보
     * @return 댓글 신고 정보의 아이디 값
     */
    public Long saveReportedComment(ReportedComment reportedComment) {
        ReportedComment savedComment = reportedCommentRepository.save(reportedComment);
        return savedComment.getId();
    }

    /**
     * 신고 사유에 따른 게시글 신고 정보 목록을 반환한다. - 페이징 적용
     * @param reportType 신고 사유
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 게시글 신고 정보 목록
     */
    public Slice<ReportedBoard> findReportedBoardsByReportType(ReportType reportType, Crew crew, Pageable pageable) {
        return reportedBoardRepository.findByCrewAndReportType(reportType, crew, pageable);
    }

    /**
     * 신고 사유에 따른 댓글 신고 정보 목록을 반환한다. - 페이징 적용
     * @param reportType 신고 사유
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 댓글 신고 정보 목록
     */
    public Slice<ReportedComment> findReportedCommentsByReportType(ReportType reportType, Crew crew, Pageable pageable) {
        return reportedCommentRepository.findByCrewAndReportType(reportType, crew, pageable);
    }



}
