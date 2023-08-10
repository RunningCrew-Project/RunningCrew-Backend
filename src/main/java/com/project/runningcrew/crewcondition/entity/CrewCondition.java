package com.project.runningcrew.crewcondition.entity;

import com.project.runningcrew.crew.entity.Crew;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@SQLDelete(sql = "update crew_conditions set deleted = true where crew_condition_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "crew_conditions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_condition_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @NotNull
    private boolean joinApply;

    @NotNull
    private boolean joinQuestion;

    @Column
    private boolean deleted = false;

    public CrewCondition(Crew crew) {
        this.crew = crew;
        this.joinApply = true;
        this.joinQuestion = false;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void updateJoinApply(boolean join) {
        this.joinApply = join;
    }

    public void updateJoinQuestion(boolean question) {
        this.joinQuestion = question;
    }

}
