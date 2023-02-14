package com.project.runningcrew.entity.comment;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.members.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue("board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment extends Comment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardComment(Member member, String detail, Board board) {
        super(member, detail);
        this.board = board;
    }

}
