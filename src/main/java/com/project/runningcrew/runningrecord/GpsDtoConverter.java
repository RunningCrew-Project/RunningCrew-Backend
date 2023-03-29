package com.project.runningcrew.runningrecord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.runningrecord.dto.GpsDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GpsDtoConverter implements Converter<String, List<GpsDto>> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public List<GpsDto> convert(String source) {
        return objectMapper.readValue(source, new TypeReference<>() {});
    }
}
