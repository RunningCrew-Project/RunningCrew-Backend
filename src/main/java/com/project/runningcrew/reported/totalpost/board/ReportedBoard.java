package com.project.runningcrew.reported.totalpost.board;

import com.project.runningcrew.reported.ReportType;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.reported.totalpost.ReportedTotalPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update reported_total_posts set deleted = true where reported_total_post_id = ?")
@Getter
@DiscriminatorValue("board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportedBoard extends ReportedTotalPost {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public ReportedBoard(Board board, Member member, ReportType reportType) {
        super(member, reportType);
        this.board = board;
    }

    public ReportedBoard(Long id, Board board, Member member, ReportType reportType) {
        super(id, member, reportType);
        this.board = board;
    }

}


