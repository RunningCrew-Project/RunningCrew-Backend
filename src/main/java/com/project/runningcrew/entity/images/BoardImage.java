package com.project.runningcrew.entity.images;

import com.project.runningcrew.entity.boards.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue("board")
@NoArgsConstructor
public class BoardImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardImage(String fileName, Board board) {
        super(fileName);
        this.board = board;
    }

    public BoardImage(Long id, String fileName, Board board) {
        super(id, fileName);
        this.board = board;
    }

}
