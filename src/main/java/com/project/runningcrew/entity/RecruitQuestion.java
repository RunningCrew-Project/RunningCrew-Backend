package com.project.runningcrew.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitQuestion extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "recruit_question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @NotBlank
    @Size(min = 1, max = 200, message = "크루 가입 질문은 1 자 이상 200 자 이하입니다.")
    @Column(nullable = false, length = 200)
    private String question;

    @PositiveOrZero(message = "순서는 0 이상입니다.")
    @Column(nullable = false)
    private int offset;

    public RecruitQuestion(Crew crew, String question, int offset) {
        this.crew = crew;
        this.question = question;
        this.offset = offset;
    }

}
