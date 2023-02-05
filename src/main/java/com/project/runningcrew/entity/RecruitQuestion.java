package com.project.runningcrew.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitQuestion extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "recruit_question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @NotBlank
    @Column(nullable = false)
    private String question;

    @Positive
    @Column(nullable = false)
    private int offset;

    public RecruitQuestion(Crew crew, String question, int offset) {
        this.crew = crew;
        this.question = question;
        this.offset = offset;
    }

}
