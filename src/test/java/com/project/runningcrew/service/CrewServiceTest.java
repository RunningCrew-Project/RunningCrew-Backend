package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.CrewNotFoundException;
import com.project.runningcrew.repository.CrewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewServiceTest {

    @Mock
    private CrewRepository crewRepository;

    @InjectMocks
    private CrewService crewService;

    @DisplayName("id 로 크루 하나 가져오기 성공 테스트")
    @Test
    public void findByIdTest1() {
        //given
        Long crewId = 1L;
        Crew crew = Crew.builder().build();

        when(crewRepository.findById(crewId)).thenReturn(Optional.of(crew));

        ///when
        Crew findCrew = crewService.findById(crewId);

        //then
        assertThat(findCrew).isEqualTo(crew);
        verify(crewRepository, times(1)).findById(crewId);
    }

    @DisplayName("id 로 크루 하나 가져오기 예외 테스트")
    @Test
    public void findByTest2() {
        //given
        Long crewId = 1L;
        when(crewRepository.findById(crewId)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> crewService.findById(crewId))
                .isInstanceOf(CrewNotFoundException.class);
        verify(crewRepository, times(1)).findById(crewId);
    }

    @DisplayName("크루 저장 테스트")
    @Test
    public void saveTest(@Mock DongArea dongArea) {
        //given
        Long crewId = 1L;
        Crew crew = Crew.builder().id(crewId)
                .name("name")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .dongArea(dongArea)
                .build();
        when(crewRepository.save(crew)).thenReturn(crew);
        //TODO MultipartFile flow 추가

        ///when
        Long saveId = crewService.saveCrew(crew);

        //then
        assertThat(saveId).isSameAs(crewId);
        verify(crewRepository, times(1)).save(crew);
    }

    @DisplayName("크루 수정 테스트")
    @Test
    public void updateCrewTest() {
        //given
        //TODO MultipartFile flow 추가

        ///when
        
        //then
    }

    @DisplayName("크루 삭제 테스트")
    @Test
    public void deleteTest(@Mock DongArea dongArea) {
        //given
        Long crewId = 1L;
        Crew crew = Crew.builder().name("name")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .dongArea(dongArea)
                .build();
        doNothing().when(crewRepository).delete(crew);
        //TODO MultipartFile flow 추가

        ///when
        crewService.deleteCrew(crew);

        //then
        verify(crewRepository, times(1)).delete(crew);
    }

    @DisplayName("키워드로 크루 찾기 첫 페이지 테스트")
    @Test
    public void findByKeywordTest1(@Mock DongArea dongArea) {
        //given
        String keyword = "duction";
        PageRequest pageRequest = PageRequest.of(0, 7);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        Slice<Crew> crewSlice = new SliceImpl<>(crews, pageRequest, true);
        when(crewRepository.findByNameOrIntroductionOrArea(pageRequest, keyword)).thenReturn(crewSlice);

        ///when
        Slice<Crew> result = crewService.findByKeyword(pageRequest, keyword);

        //then
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
        verify(crewRepository, times(1)).findByNameOrIntroductionOrArea(pageRequest, keyword);
    }

    @DisplayName("키워드로 크루 찾기 중간 페이지 테스트")
    @Test
    public void findByKeywordTest2(@Mock DongArea dongArea) {
        //given
        String keyword = "duction";
        PageRequest pageRequest = PageRequest.of(1, 7);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        Slice<Crew> crewSlice = new SliceImpl<>(crews, pageRequest, true);
        when(crewRepository.findByNameOrIntroductionOrArea(pageRequest, keyword)).thenReturn(crewSlice);

        ///when
        Slice<Crew> result = crewService.findByKeyword(pageRequest, keyword);

        //then
        assertThat(result.getNumber()).isSameAs(1);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isLast()).isFalse();
        verify(crewRepository, times(1)).findByNameOrIntroductionOrArea(pageRequest, keyword);
    }

    @DisplayName("키워드로 크루 찾기 마지막 페이지 테스트")
    @Test
    public void findByKeywordTest3(@Mock DongArea dongArea) {
        //given
        String keyword = "duction";
        PageRequest pageRequest = PageRequest.of(2, 7);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        Slice<Crew> crewSlice = new SliceImpl<>(crews, pageRequest, false);
        when(crewRepository.findByNameOrIntroductionOrArea(pageRequest, keyword)).thenReturn(crewSlice);

        ///when
        Slice<Crew> result = crewService.findByKeyword(pageRequest, keyword);

        //then
        assertThat(result.getNumber()).isSameAs(2);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(5);
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.hasNext()).isFalse();
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isLast()).isTrue();
        verify(crewRepository, times(1)).findByNameOrIntroductionOrArea(pageRequest, keyword);
    }

    @DisplayName("동 id 로 랜덤한 크루 maxSize 개 가져오기 테스트")
    @Test
    public void findRandomByDongAreaIdTest(@Mock GuArea guArea) {
        //given
        Long dongId = 1L;
        int maxSize = 10;
        DongArea dongArea = new DongArea(dongId, "dong1", guArea);
        List<Crew> crews = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImgUrl" + i)
                    .dongArea(dongArea)
                    .build();
            crews.add(crew);
        }
        when(crewRepository.findRandomByDongAreaId(dongId, maxSize)).thenReturn(crews);

        ///when
        List<Crew> crewList = crewService.findRandomByDongAreaId(dongId, maxSize);

        //then
        assertThat(crewList.size()).isLessThanOrEqualTo(maxSize);
        for (Crew crew : crewList) {
            assertThat(crew.getDongArea().getId()).isSameAs(dongId);
        }
        verify(crewRepository, times(1)).findRandomByDongAreaId(dongId, maxSize);
    }

    @DisplayName("유저가 속한 모든 크루 찾기 테스트")
    @Test
    public void findAllByUserTest(@Mock User user, @Mock DongArea dongArea) {
        //given
        List<Crew> crews = new ArrayList<>();
        for (int i = 50; i < 60; i++) {
            Crew crew = Crew.builder().name("crew" + i)
                    .introduction("introduction" + i)
                    .crewImgUrl("crewImageUrl")
                    .dongArea(dongArea)
                    .build();
        }
        when(crewRepository.findAllByUser(user)).thenReturn(crews);

        ///when
        List<Crew> crewList = crewService.findAllByUser(user);

        //then
        assertThat(crewList.size()).isSameAs(crews.size());
        verify(crewRepository, times(1)).findAllByUser(user);
    }

}