package com.project.runningcrew.recruitquestion.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.crew.entity.Crew;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@SQLDelete(sql = "update recruit_questions set deleted = true where recruit_question_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "recruit_questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column
    private boolean deleted = false;

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

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
