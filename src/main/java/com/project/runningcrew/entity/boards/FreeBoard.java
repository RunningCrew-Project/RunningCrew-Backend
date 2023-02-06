package com.project.runningcrew.entity.boards;

import com.project.runningcrew.entity.members.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("free")
@NoArgsConstructor
public class FreeBoard extends Board{

    public FreeBoard(Member member, String title, String content) {
        super(member, title, content);
    }

}
