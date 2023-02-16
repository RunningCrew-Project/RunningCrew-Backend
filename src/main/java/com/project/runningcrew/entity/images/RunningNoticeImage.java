package com.project.runningcrew.entity.images;

import com.project.runningcrew.entity.runningnotices.RunningNotice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
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
