package com.project.runningcrew.entity;

import com.project.runningcrew.entity.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitAnswer extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "recruit_answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @NotBlank
    @Column(nullable = false)
    private String answer;

    public RecruitAnswer(User user, Crew crew, String answer) {
        this.user = user;
        this.crew = crew;
        this.answer = answer;
    }

}
