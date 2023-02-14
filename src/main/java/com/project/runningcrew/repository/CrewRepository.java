package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {

    /**
     * name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 반환
     * @param keyword
     * @return name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 list
     */
    @Query(value = "select c from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.dongArea.name like %:keyword% " +
            "or c.dongArea.guArea.name like %:keyword% " +
            "or c.dongArea.guArea.sidoArea.name like %:keyword%")
    List<Crew> findAllByNameOrIntroductionOrArea(@Param("keyword") String keyword);

    /**
     * name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 개수 반환
     * @param keyword
     * @return name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 개수
     */
    @Query(value = "select count(c) from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.dongArea.name like %:keyword% " +
            "or c.dongArea.guArea.name like %:keyword% " +
            "or c.dongArea.guArea.sidoArea.name like %:keyword%")
    Long countAllByNameOrIntroductionOrArea(@Param("keyword") String keyword);

    /**
     * name 또는 introduction 또는 area 에 keyword 가 포함된 crew 들을 페이징하여 반환
     * @param pageable
     * @param keyword
     * @return 페이징 조건에 맞는 name 또는 introduction 또는 area 에 keyword 가 포함된 crew
     * 들이 담긴 Slice
     */
    @Query(value = "select c from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.dongArea.name like %:keyword% " +
            "or c.dongArea.guArea.name like %:keyword% " +
            "or c.dongArea.guArea.sidoArea.name like %:keyword%")
    Slice<Crew> findByNameOrIntroductionOrArea(Pageable pageable, @Param("keyword") String keyword);

    /**
     * 특정 dongArea 에 포함되는 랜덤한 crew 들을 최대 maxsize 개 반환
     * @param dongAreaId dongArea 의 id
     * @param maxSize 반환할 crew 의 최대 개수
     * @return dongArea 에 포함되는 랜덤한 crew 들이 최대 maxsize 개 담긴 list
     */
    @Query(value = "select * from crews where crews.dong_area_id = :dongAreaId" +
            " order by random() limit :maxSize", nativeQuery = true)
    List<Crew> findRandomByDongAreaId(@Param("dongAreaId") Long dongAreaId, @Param("maxSize") int maxSize);

}
