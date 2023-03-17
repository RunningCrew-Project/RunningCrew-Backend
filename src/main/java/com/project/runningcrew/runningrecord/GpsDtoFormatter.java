package com.project.runningcrew.runningrecord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.runningcrew.runningrecord.dto.GpsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GpsDtoFormatter implements Formatter<List<GpsDto>> {

    private final ObjectMapper objectMapper;

    @Override
    public List<GpsDto> parse(String text, Locale locale) throws ParseException {
        try {
            return objectMapper.readValue(text, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String print(List<GpsDto> object, Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < object.size(); i++) {
            sb.append(object.get(i).toString());
            if (i != object.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
