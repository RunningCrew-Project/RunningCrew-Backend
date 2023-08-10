package com.project.runningcrew.resourceimage.entity;

import com.project.runningcrew.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@SQLDelete(sql = "update images set deleted = true where image_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "images")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @NotBlank(message = "이미지 이름은 필수값입니다.")
    @Size(min = 1, max = 200, message = "이미지 이름은 1 자 이상 200 자 이하입니다.")
    @Column(nullable = false, length = 200)
    private String fileName;

    @Column
    private boolean deleted = false;

    public Image(String fileName) {
        this.fileName = fileName;
    }

    public Image(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
