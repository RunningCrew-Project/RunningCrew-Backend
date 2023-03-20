package com.project.runningcrew.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LongListConverter implements Converter<String, List<Long>> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public List<Long> convert(String source) {
        return objectMapper.readValue(source, new TypeReference<>() {});
    }
}
