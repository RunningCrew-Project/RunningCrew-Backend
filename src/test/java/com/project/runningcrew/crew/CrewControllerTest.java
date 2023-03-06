package com.project.runningcrew.crew;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.service.CrewService;
import com.project.runningcrew.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CrewController.class)
class CrewControllerTest {

    @MockBean
    private CrewService crewService;

    @MockBean
    private MemberService memberService;

    @MockBean
    DongArea dongArea;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser
    @DisplayName("크루 가져오기 성공 테스트")
    @Test
    public void getCrewTest1() throws Exception {
        //given
        Long crewId = 1L;
        String name = "name";
        String introduction = "introduction";
        String crewImgUrl = "crewImgUrl";
        String dongFullName = "서울시 동대문구 전농동";
        Long memberCount = 10L;
        Crew crew = Crew.builder()
                .id(crewId)
                .name(name)
                .introduction(introduction)
                .crewImgUrl(crewImgUrl)
                .dongArea(dongArea)
                .build();
        when(crewService.findById(crewId)).thenReturn(crew);
        when(memberService.countAllByCrew(crew)).thenReturn(memberCount);
        when(dongArea.getFullName()).thenReturn(dongFullName);

        ///when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/crews/%d", crewId)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(crewId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.introduction").value(introduction))
                .andExpect(jsonPath("$.crewImgUrl").value(crewImgUrl))
                .andExpect(jsonPath("$.dong").value(dongFullName))
                .andExpect(jsonPath("$.memberCount").value(memberCount));

    }

}