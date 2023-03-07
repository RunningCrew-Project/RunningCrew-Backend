package com.project.runningcrew.area.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "sido_areas")
@NoArgsConstructor
public class SidoArea {

    @Id
    @GeneratedValue
    @Column(name = "sido_area_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    public SidoArea(String name) {
        this.name = name;
    }

    public SidoArea(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
