package com.project.runningcrew.area.repository;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DongAreaRepository extends JpaRepository<DongArea, Long> {

    /**
     * 특정 GuArea 를 가진 모든 DongArea 들을 이름순으로 오름차순 정리하여 반환
     *
     * @param guArea
     * @return 특정 GuArea 를 가진 모든 DongArea 들이 담긴 name 순으로 오름차순 정렬된 list
     */
    List<DongArea> findAllByGuAreaOrderByNameAsc(GuArea guArea);

    /**
     * 특정 name 을 가진 DongArea 를 찾아 반환
     *
     * @param name 찾는 DongArea 의 name
     * @return 특정 name 을 가진 DongArea. 없다면 Optional.empty()
     */
    Optional<DongArea> findByName(String name);

    /**
     * 특정 구에 속하고 특정 name 을 가진 DongArea 를 찾아 반환
     *
     * @param guArea 특정 구
     * @param name 찾는 DongArea 의 name
     * @return 특정 구에 속하고 특정 name 을 가진 DongArea. 없다면 Optional.empty()
     */
    Optional<DongArea> findByGuAreaAndName(GuArea guArea, String name);

}
