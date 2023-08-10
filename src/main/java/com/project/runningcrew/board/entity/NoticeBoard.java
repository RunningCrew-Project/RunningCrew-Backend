package com.project.runningcrew.board.entity;

import com.project.runningcrew.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@SQLDelete(sql = "update boards set deleted = true where board_id = ?")
@Getter
@DiscriminatorValue("notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeBoard extends Board {

    public NoticeBoard(Member member, String title, String content) {
        super(member, title, content);
    }

    public NoticeBoard(Long id, Member member, String title, String detail) {
        super(id, member, title, detail);
    }
    
}
