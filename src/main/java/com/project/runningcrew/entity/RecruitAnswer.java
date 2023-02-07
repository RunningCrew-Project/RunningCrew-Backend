package com.project.runningcrew.entity;

import com.project.runningcrew.entity.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Getter
@Table(name = "recruit_answers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitAnswer extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "recruit_answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @NotBlank
    @Size(min = 1, max = 200, message = "크루 가입 답변은 1 자 이상 200 자 이하입니다.")
    @Column(nullable = false, length = 200)
    private String answer;

    @PositiveOrZero(message = "순서는 0 이상입니다.")
    @Column(nullable = false)
    private int answerOffset;

    public RecruitAnswer(User user, Crew crew, String answer, int answerOffset) {
        this.user = user;
        this.crew = crew;
        this.answer = answer;
        this.answerOffset = answerOffset;
    }

}
