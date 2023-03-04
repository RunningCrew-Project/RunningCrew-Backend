package com.project.runningcrew.repository.areas;

import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuAreaRepository extends JpaRepository<GuArea, Long> {

    /**
     * 특정 SidoArea 를 가진 모든 GuArea 들을 이름순으로 오름차순 정리하여 반환
     *
     * @param sidoArea
     * @return 특정 SidoArea 를 가진 모든 GuArea 들이 담긴 name 순으로 오름차순 정렬된 list
     */
    List<GuArea> findAllBySidoAreaOrderByNameAsc(SidoArea sidoArea);

    /**
     * 특정 시/도에 속한 특정 name 을 가진 GuArea 를 찾아 반환
     *
     * @param sidoArea 특정 시/도
     * @param name 찾는 GuArea 의 name
     * @return 특정 시/도에 속한 특정 name 을 가진 GuArea. 없다면 Optional.empty()
     */
    Optional<GuArea> findBySidoAreaAndName(SidoArea sidoArea, String name);

}
