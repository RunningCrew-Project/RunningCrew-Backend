package com.project.runningcrew.comment.entity;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update comments set deleted = true where comment_id = ?")
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

    public BoardComment(Long id, Member member, String detail, Board board) {
        super(id, member, detail);
        this.board = board;
    }

}
