package com.project.runningcrew.repository.areas;

import com.project.runningcrew.entity.areas.SidoArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SidoAreaRepository extends JpaRepository<SidoArea, Long> {

    List<SidoArea> findAllByOrderByNameAsc();

}
