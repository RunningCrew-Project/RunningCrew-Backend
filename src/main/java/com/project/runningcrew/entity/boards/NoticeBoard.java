package com.project.runningcrew.entity.boards;

import com.project.runningcrew.entity.members.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
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
