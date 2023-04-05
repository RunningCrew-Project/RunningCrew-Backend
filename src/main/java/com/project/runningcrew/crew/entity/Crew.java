package com.project.runningcrew.crew.entity;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Table(name = "crews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id")
    private Long id;

    @NotBlank(message = "크루 이름은 필수값입니다.")
    @Size(min = 1, max = 100, message = "크루 이름은 1 자 이상 100 자 이하입니다.")
    @Column(unique = true, nullable = false, length = 100)
    private String name;


    @NotBlank(message = "크루 소개는 필수값입니다.")
    @Size(min = 1, max = 500, message = "크루 소개는 1 자 이상 500 자 이하입니다.")
    @Column(nullable = false, length = 500)
    private String introduction;

    @NotBlank(message = "이미지 경로는 필수값입니다.")
    @Column(nullable = false)
    private String crewImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_area_id", nullable = false)
    private DongArea dongArea;

    @Builder
    public Crew(Long id, String name, String introduction, String crewImgUrl, DongArea dongArea) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.crewImgUrl = crewImgUrl;
        this.dongArea = dongArea;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDongArea(DongArea dongArea) {
        this.dongArea = dongArea;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateCrewImgUrl(String crewImgUrl) {
        this.crewImgUrl = crewImgUrl;
    }

}
