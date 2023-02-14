package com.project.runningcrew.repository.areas;

import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuAreaRepository extends JpaRepository<GuArea, Long> {

    /**
     * 특정 SidoArea 를 가진 모든 GuArea 들을 이름순으로 오름차순 정리하여 반환
     * @param sidoArea
     * @return 특정 SidoArea 를 가진 모든 GuArea 들이 담긴 name 순으로 오름차순 정렬된 list
     */
    List<GuArea> findAllBySidoAreaOrderByNameAsc(SidoArea sidoArea);

}
