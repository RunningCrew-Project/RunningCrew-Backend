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

    @Query(value = "select c from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.location like %:keyword%")
    List<Crew> findAllByNameOrIntroductionOrLocation(@Param("keyword") String keyword);

    @Query(value = "select count(c) from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.location like %:keyword%")
    Long countAllByNameOrIntroductionOrLocation(@Param("keyword") String keyword);

    @Query(value = "select c from Crew c where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or c.location like %:keyword%")
    Slice<Crew> findByNameOrIntroductionOrLocation(Pageable pageable, @Param("keyword") String keyword);

    @Query(value = "select * from crews where crews.location = :location" +
            " order by random() limit :maxSize", nativeQuery = true)
    List<Crew> findRandomByLocation(@Param("location") String location, @Param("maxSize") int maxSize);

}
