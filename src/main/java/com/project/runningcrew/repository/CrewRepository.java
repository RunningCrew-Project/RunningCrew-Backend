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
     * name 또는 introduction 또는 location 에 keyword 가 포함된 모든 crew 반환
     * @param keyword
     * @return name 또는 introduction 또는 location 에 keyword 가 포함된 모든 crew 의 list
     */
    @Query(value = "select c from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.location like %:keyword%")
    List<Crew> findAllByNameOrIntroductionOrLocation(@Param("keyword") String keyword);

    /**
     * name 또는 introduction 또는 location 에 keyword 가 포함된 모든 crew 의 개수 반환
     * @param keyword
     * @return name 또는 introduction 또는 location 에 keyword 가 포함된 모든 crew 의 개수
     */
    @Query(value = "select count(c) from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.location like %:keyword%")
    Long countAllByNameOrIntroductionOrLocation(@Param("keyword") String keyword);

    /**
     * name 또는 introduction 또는 location 에 keyword 가 포함된 crew 들을 페이징하여 반환
     * @param pageable
     * @param keyword
     * @return 페이징 조건에 맞는 name 또는 introduction 또는 location 에 keyword 가 포함된 crew
     * 들이 담긴 Slice
     */
    @Query(value = "select c from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.location like %:keyword%")
    Slice<Crew> findByNameOrIntroductionOrLocation(Pageable pageable, @Param("keyword") String keyword);

    /**
     * location 에 포함되는 랜덤한 crew 들을 최대 maxsize 개 반환
     * @param location
     * @param maxSize 반환할 crew 의 최대 개수
     * @return location 에 포함되는 랜덤한 crew 들이 최대 maxsize 개 담긴 list
     */
    @Query(value = "select * from crews where crews.location = :location" +
            " order by random() limit :maxSize", nativeQuery = true)
    List<Crew> findRandomByLocation(@Param("location") String location, @Param("maxSize") int maxSize);

}
