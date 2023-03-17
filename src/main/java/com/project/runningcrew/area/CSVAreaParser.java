package com.project.runningcrew.area;

import com.opencsv.CSVReader;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.area.repository.DongAreaRepository;
import com.project.runningcrew.area.repository.GuAreaRepository;
import com.project.runningcrew.area.repository.SidoAreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CSVAreaParser {

    private final SidoAreaRepository sidoAreaRepository;
    private final GuAreaRepository guAreaRepository;
    private final DongAreaRepository dongAreaRepository;
    @Value("${area.file.path}")
    private String path;

    /**
     * 법정동 정보를 가진 csv 로 시/도, 구, 동 엔티티를 생성한다.
     */
    public void createAreas() {
        Map<String, SidoArea> sidoMap = new HashMap<>();
        Map<Long, Map<String, GuArea>> guMap = new HashMap<>();
        Map<Long, Map<String, DongArea>> dongMap = new HashMap<>();

        ClassPathResource resource = new ClassPathResource(path);
        String[] areaInfo;
        try (CSVReader csvReader = new CSVReader(new FileReader(resource.getFile()))) {
            while ((areaInfo = csvReader.readNext()) != null) {
                String sidoName = areaInfo[0];
                if (!sidoMap.containsKey(sidoName)) {
                    SidoArea sidoArea = sidoAreaRepository.findByName(sidoName)
                            .orElseGet(() -> sidoAreaRepository.save(new SidoArea(sidoName)));
                    sidoMap.put(sidoName, sidoArea);
                }

                SidoArea sidoArea = sidoMap.get(sidoName);
                Long sidoId = sidoArea.getId();
                String guName = areaInfo[1];
                if (!guMap.containsKey(sidoId)) {
                    guMap.put(sidoId, new HashMap<>());
                }
                if (!guMap.get(sidoId).containsKey(guName)) {
                    GuArea guArea = guAreaRepository.findBySidoAreaAndName(sidoArea, guName)
                            .orElseGet(() -> guAreaRepository.save(new GuArea(guName, sidoArea)));
                    guMap.get(sidoId).put(guName, guArea);
                }

                GuArea guArea = guMap.get(sidoId).get(guName);
                Long guId = guArea.getId();
                String dongName = areaInfo[2];
                if (!dongMap.containsKey(guId)) {
                    dongMap.put(guId, new HashMap<>());
                }
                if (!dongMap.get(guId).containsKey(dongName)) {
                    DongArea dongArea = dongAreaRepository.findByGuAreaAndName(guArea, dongName)
                            .orElseGet(() -> dongAreaRepository.save(new DongArea(dongName, guArea)));
                    dongMap.get(guId).put(dongName, dongArea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

}
