package com.project.runningcrew.entity.areas;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "dong_areas")
@NoArgsConstructor
public class DongArea {

    @Id
    @GeneratedValue
    @Column(name = "dong_area_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gu_areas_id", nullable = false)
    private GuArea guArea;

    public DongArea(String name, GuArea guArea) {
        this.name = name;
        this.guArea = guArea;
    }

    public DongArea(Long id, String name, GuArea guArea) {
        this.id = id;
        this.name = name;
        this.guArea = guArea;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.guArea.getSidoArea().getName());
        sb.append(" ");
        sb.append(this.guArea.getName());
        sb.append(" ");
        sb.append(this.name);
        return sb.toString();
    }

}
