package com.project.runningcrew.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "crew_id")
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String location;

    @NotBlank
    @Column(nullable = false)
    private String introduction;

    @NotBlank
    @Column(nullable = false)
    private String crewImgUrl;

    @Builder
    public Crew(String name, String location, String introduction, String crewImgUrl) {
        this.name = name;
        this.location = location;
        this.introduction = introduction;
        this.crewImgUrl = crewImgUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateCrewImgUrl(String crewImgUrl) {
        this.crewImgUrl = crewImgUrl;
    }

}
