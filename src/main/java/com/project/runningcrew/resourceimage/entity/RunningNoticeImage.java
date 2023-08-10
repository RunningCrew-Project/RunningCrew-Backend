package com.project.runningcrew.resourceimage.entity;

import com.project.runningcrew.runningnotice.entity.RunningNotice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update images set deleted = true where image_id = ?")
@Getter
@DiscriminatorValue("running_notice")
@NoArgsConstructor
public class RunningNoticeImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_notice_id")
    private RunningNotice runningNotice;

    public RunningNoticeImage(String fileName, RunningNotice runningNotice) {
        super(fileName);
        this.runningNotice = runningNotice;
    }

    public RunningNoticeImage(Long id, String fileName, RunningNotice runningNotice) {
        super(id, fileName);
        this.runningNotice = runningNotice;
    }

}
