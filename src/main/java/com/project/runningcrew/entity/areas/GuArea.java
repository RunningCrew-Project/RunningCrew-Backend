package com.project.runningcrew.entity.areas;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "gu_areas")
@NoArgsConstructor
public class GuArea {

    @Id
    @GeneratedValue
    @Column(name = "gu_area_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_areas_id", nullable = false)
    private SidoArea sidoArea;

    public GuArea(String name, SidoArea sidoArea) {
        this.name = name;
        this.sidoArea = sidoArea;
    }

    public GuArea(Long id, String name, SidoArea sidoArea) {
        this.id = id;
        this.name = name;
        this.sidoArea = sidoArea;
    }

}
