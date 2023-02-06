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
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "crew_id")
    private Long id;

    @NotBlank(message = "크루 이름은 필수값입니다.")
    @Size(min = 1, max = 100, message = "크루 이름은 1 자 이상 100 자 이하입니다.")
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @NotBlank(message = "활동 지역은 필수값입니다.")
    @Size(min = 1, max = 100, message = "활동 지역은 1 자 이상 100 자 이하입니다.")
    @Column(nullable = false, length = 100)
    private String location;

    @NotBlank(message = "크루 소개는 필수값입니다.")
    @Size(min = 1, max = 500, message = "크루 소개는 1 자 이상 500 자 이하입니다.")
    @Column(nullable = false, length = 500)
    private String introduction;

    @NotBlank(message = "이미지 경로는 필수값입니다.")
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
