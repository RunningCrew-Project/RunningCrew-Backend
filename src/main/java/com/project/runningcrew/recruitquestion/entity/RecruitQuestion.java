package com.project.runningcrew.recruitquestion.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.crew.entity.Crew;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Table(name = "recruit_questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitQuestion extends BaseEntity {

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
    private int questionOffset;

    public RecruitQuestion(Crew crew, String question, int questionOffset) {
        this.crew = crew;
        this.question = question;
        this.questionOffset = questionOffset;
    }

    public RecruitQuestion(Long id, Crew crew, String question, int questionOffset) {
        this.id = id;
        this.crew = crew;
        this.question = question;
        this.questionOffset = questionOffset;
    }

}
