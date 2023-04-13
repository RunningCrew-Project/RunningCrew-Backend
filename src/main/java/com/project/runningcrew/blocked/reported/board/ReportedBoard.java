package com.project.runningcrew.blocked.reported.board;

import com.project.runningcrew.blocked.reported.ReportType;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "reported_boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportedBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reported_board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    public ReportedBoard(Board board, Member member, ReportType reportType) {
        this.board = board;
        this.member = member;
        this.reportType = reportType;
    }

    public ReportedBoard(Long id, Board board, Member member) {
        this.id = id;
        this.board = board;
        this.member = member;
    }

}


