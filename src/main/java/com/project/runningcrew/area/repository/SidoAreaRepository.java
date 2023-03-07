package com.project.runningcrew.area.repository;

import com.project.runningcrew.area.entity.SidoArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SidoAreaRepository extends JpaRepository<SidoArea, Long> {

    /**
     * 모든 SidoArea 들을 이름순으로 오름차순 정리하여 반환
     * @return 모든 SidoArea 들이 담긴 name 순으로 오름차순 정렬된 list
     */
    List<SidoArea> findAllByOrderByNameAsc();

    /**
     * 특정 name 을 가진 SidoArea 를 찾아 반환
     *
     * @param name : 찾는 SidoArea 의 name
     * @return 특정 name 을 가진 SidoArea. 없다면 Optional.empty()
     */
    Optional<SidoArea> findByName(String name);

}
