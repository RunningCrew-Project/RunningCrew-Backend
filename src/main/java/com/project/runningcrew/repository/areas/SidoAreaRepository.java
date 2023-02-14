package com.project.runningcrew.repository.areas;

import com.project.runningcrew.entity.areas.SidoArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SidoAreaRepository extends JpaRepository<SidoArea, Long> {

    /**
     * 모든 SidoArea 들을 이름순으로 오름차순 정리하여 반환
     * @return 모든 SidoArea 들이 담긴 name 순으로 오름차순 정렬된 list
     */
    List<SidoArea> findAllByOrderByNameAsc();

}
