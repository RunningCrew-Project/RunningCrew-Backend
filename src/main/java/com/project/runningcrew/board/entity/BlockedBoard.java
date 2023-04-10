package com.project.runningcrew.board.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "blocked_boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocked_board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public BlockedBoard(Board board, Member member) {
        this.board = board;
        this.member = member;
    }

    public BlockedBoard(Long id, Board board, Member member) {
        this.id = id;
        this.board = board;
        this.member = member;
    }

}

