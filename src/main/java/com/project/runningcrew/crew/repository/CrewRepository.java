package com.project.runningcrew.crew.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.user.entity.User;
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
     *
     * @param keyword
     * @return name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 list
     */
    @Query(value = "select c from Crew c " +
            "inner join DongArea d on c.dongArea = d " +
            "inner join GuArea g on d.guArea = g " +
            "inner join SidoArea s on g.sidoArea = s " +
            "where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or d.name like %:keyword% " +
            "or g.name like %:keyword% " +
            "or s.name like %:keyword%")
    List<Crew> findAllByNameOrIntroductionOrArea(@Param("keyword") String keyword);

    /**
     * name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 개수 반환
     *
     * @param keyword
     * @return name 또는 introduction 또는 area 에 keyword 가 포함된 모든 crew 의 개수
     */
    @Query(value = "select count(c) from Crew c " +
            "inner join DongArea d on c.dongArea = d " +
            "inner join GuArea g on d.guArea = g " +
            "inner join SidoArea s on g.sidoArea = s " +
            "where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or d.name like %:keyword% " +
            "or g.name like %:keyword% " +
            "or s.name like %:keyword%")
    Long countAllByNameOrIntroductionOrArea(@Param("keyword") String keyword);

    /**
     * name 또는 introduction 또는 area 에 keyword 가 포함된 crew 들을 페이징하여 반환
     *
     * @param pageable
     * @param keyword
     * @return 페이징 조건에 맞는 name 또는 introduction 또는 area 에 keyword 가 포함된 crew
     * 들이 담긴 Slice
     */
    @Query(value = "select count(c) from Crew c " +
            "inner join DongArea d on c.dongArea = d " +
            "inner join GuArea g on d.guArea = g " +
            "inner join SidoArea s on g.sidoArea = s " +
            "where c.name like %:keyword% " +
            "or c.introduction like %:keyword% " +
            "or d.name like %:keyword% " +
            "or g.name like %:keyword% " +
            "or s.name like %:keyword%")
    Slice<Crew> findByNameOrIntroductionOrArea(Pageable pageable, @Param("keyword") String keyword);

    /**
     * 특정 dongArea 에 포함되는 랜덤한 crew 들을 최대 maxsize 개 반환
     *
     * @param dongAreaId dongArea 의 id
     * @param maxSize    반환할 crew 의 최대 개수
     * @return dongArea 에 포함되는 랜덤한 crew 들이 최대 maxsize 개 담긴 list
     */
    @Query(value = "select * from crews where crews.dong_area_id = :dongAreaId " +
            "order by rand() limit :maxSize", nativeQuery = true)
    List<Crew> findRandomByDongAreaId(@Param("dongAreaId") Long dongAreaId, @Param("maxSize") int maxSize);

    /**
     * 특정 guArea 에 포함되는 랜덤한 crew 들을 최대 maxsize 개 반환
     *
     * @param guAreaId guArea 의 id
     * @param maxSize  반환할 crew 의 최대 개수
     * @return dongArea 에 포함되는 랜덤한 crew 들이 최대 maxsize 개 담긴 list
     */
    @Query(value = "select * from crews as c " +
            "inner join dong_areas as d on d.dong_area_id = c.dong_area_id and c.deleted = false " +
            "where d.gu_areas_id = :guAreaId " +
            "order by rand() limit :maxSize", nativeQuery = true)
    List<Crew> findRandomByGuAreaId(@Param("guAreaId") Long guAreaId, @Param("maxSize") int maxSize);

    /**
     * 입력받은 User 가 속한 모든 Crew 를 반환
     *
     * @param user
     * @return User 가 속한 모든 Crew
     */
    @Query(value = "select c from Member m " +
            "inner join Crew c on m.crew = c and c.deleted = false " +
            "inner join User u on m.user = u and u.deleted = false " +
            "where u = :user ")
    List<Crew> findAllByUser(@Param("user") User user);

    /**
     * 크루 이름이 name 인 크루 존재 유무 반환
     *
     * @param name 크루 이름
     * @return 크루 이름이 name 인 크루가 있다면 true, 없다면 false
     */
    boolean existsByName(String name);

}
