package com.project.runningcrew.repository.areas;

import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongAreaRepository extends JpaRepository<DongArea, Long> {

    List<DongArea> findAllByGuAreaOrderByNameAsc(GuArea guArea);


}
