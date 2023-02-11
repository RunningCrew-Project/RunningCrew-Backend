package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CrewRepositoryTest {

    @Autowired
    CrewRepository crewRepository;

    @BeforeEach
    public void saveTestCrews() {
        for (int i = 0; i < 25; i++) {
            Crew crew = Crew.builder().name("crew"+i)
                    .location("location1")
                    .introduction("introduction"+i)
                    .crewImgUrl("crewImageUrl"+i)
                    .build();
            crewRepository.save(crew);
        }

        for (int i = 25; i < 50; i++) {
            Crew crew = Crew.builder().name("crew"+i)
                    .location("location2")
                    .introduction("introduction"+i)
                    .crewImgUrl("crewImageUrl"+i)
                    .build();
            crewRepository.save(crew);
        }
    }

    @Test
    public void saveTest() {
        //given
        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();

        ///when
        Crew savedCrew = crewRepository.save(crew);

        //then
        assertThat(savedCrew).isEqualTo(savedCrew);
    }

    @Test
    public void findByIdTest() {
        //given
        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();

        ///when
        crewRepository.save(crew);
        Optional<Crew> optCrew = crewRepository.findById(crew.getId());

        //then
        assertThat(optCrew).isNotEmpty();
        assertThat(optCrew).hasValue(crew);
    }

    @Test
    public void deleteTest() {
        //given
        Crew crew = Crew.builder().name("crew")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImageUrl")
                .build();

        ///when
        Crew savedCrew = crewRepository.save(crew);
        crewRepository.delete(crew);

        //then
        Optional<Crew> optCrew = crewRepository.findById(crew.getId());
        assertThat(optCrew).isEmpty();
    }

    @Test
    public void findAllByNameOrIntroductionOrLocationTest() {
        //given
        String keyword = "duction1";

        ///when
        List<Crew> crewList = crewRepository.findAllByNameOrIntroductionOrLocation(keyword);

        //then
        assertThat(crewList.size()).isEqualTo(11);
    }

    @Test
    public void countAllByNameOrIntroductionOrLocationTest() {
        //given
        String keyword = "duction1";

        ///when
        Long count = crewRepository.countAllByNameOrIntroductionOrLocation(keyword);

        //then
        assertThat(count).isEqualTo(11L);
    }

    @Test
    public void findByNameOrIntroductionOrLocationTest() {
        //given
        int size = 15;
        PageRequest pageRequest = PageRequest.of(0, size);
        String keyword = "duction1";

        ///when
        Slice<Crew> slice = crewRepository.findByNameOrIntroductionOrLocation(pageRequest, keyword);

        //then
        assertThat(slice.getSize()).isEqualTo(size);
    }

    @Test
    public void findRandomByLocationTest() {
        //given
        int maxNum = 5;
        String location = "location1";

        ///when
        List<Crew> randomCrewList = crewRepository.findRandomByLocation(location, maxNum);

        //then
        assertThat(randomCrewList.size()).isSameAs(maxNum);
        for (Crew crew : randomCrewList) {
            assertThat(crew.getLocation()).isEqualTo(location);
        }
    }

}