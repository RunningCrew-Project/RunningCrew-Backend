package com.project.runningcrew.reported;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.reported.totalpost.board.ReportedBoard;
import com.project.runningcrew.reported.totalpost.board.ReportedBoardRepository;
import com.project.runningcrew.reported.comment.ReportedComment;
import com.project.runningcrew.reported.comment.ReportedCommentRepository;
import com.project.runningcrew.reported.totalpost.runningnotice.ReportedRunningNotice;
import com.project.runningcrew.reported.totalpost.runningnotice.ReportedRunningNoticeRepository;
import com.project.runningcrew.reported.totalpost.ReportedTotalPost;
import com.project.runningcrew.reported.totalpost.ReportedTotalPostRepository;
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


    private final ReportedBoardRepository reportedBoardRepository;
    private final ReportedRunningNoticeRepository reportedRunningNoticeRepository;
    private final ReportedCommentRepository reportedCommentRepository;

    private final ReportedTotalPostRepository reportedTotalPostRepository;


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
     * 입력받은 런닝 공지 신고 정보를 저장한다.
     * @param reportedRunningNotice 런닝 공지 신고 정보
     * @return 런닝 공지 신고 정보의 아이디 값
     */
    public Long saveReportedRunningNotice(ReportedRunningNotice reportedRunningNotice) {
        ReportedRunningNotice savedRunningNotice = reportedRunningNoticeRepository.save(reportedRunningNotice);
        return savedRunningNotice.getId();
    }

    /**
     * 입력받은 크루의 신고글 정보 전체 목록을 반환한다. - 페이징 적용
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 신고글 정보 목록
     */
    public Slice<ReportedTotalPost> findReportedTotalPostsByCrew(Crew crew, Pageable pageable) {
        return reportedTotalPostRepository.findByCrew(crew, pageable);
    }

    /**
     * 입력받은 크루의 댓글 신고 정보 전체 목록을 반환한다. - 페이징 적용
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 댓글 신고 정보 목록
     */
    public Slice<ReportedComment> findReportedCommentsByCrew(Crew crew, Pageable pageable) {
        return reportedCommentRepository.findByCrew(crew, pageable);
    }


}
