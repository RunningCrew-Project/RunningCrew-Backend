package com.project.runningcrew.repository.areas;

import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.repository.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GuAreaRepositoryTest {

    @Autowired
    GuAreaRepository guAreaRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("GuArea save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = new GuArea("gu", sidoArea);

        ///when
        GuArea savedGuArea = guAreaRepository.save(guArea);

        //then
        assertThat(savedGuArea).isEqualTo(guArea);
    }

    @DisplayName("GuArea findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = new GuArea("gu", sidoArea);
        guAreaRepository.save(guArea);

        ///when
        Optional<GuArea> optGuArea = guAreaRepository.findById(guArea.getId());

        //then
        assertThat(optGuArea).isNotEmpty();
        assertThat(optGuArea).hasValue(guArea);
    }

    @DisplayName("GuArea delete 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = new GuArea("gu", sidoArea);
        guAreaRepository.save(guArea);

        ///when
        guAreaRepository.delete(guArea);

        //then
        Optional<GuArea> optGuArea = guAreaRepository.findById(guArea.getId());
        assertThat(optGuArea).isEmpty();
    }

    @DisplayName("GuArea findAllBySidoAreaOrOrderByNameAsc 테스트")
    @Test
    public void findAllBySidoAreaOrOrderByNameAscTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        for (int i = 0; i < 10; i++) {
            GuArea guArea = new GuArea("gu" + i, sidoArea);
            guAreaRepository.save(guArea);
        }

        ///when
        List<GuArea> guAreas = guAreaRepository.findAllBySidoAreaOrderByNameAsc(sidoArea);

        //then
        assertThat(guAreas.size()).isEqualTo(10);
    }

}