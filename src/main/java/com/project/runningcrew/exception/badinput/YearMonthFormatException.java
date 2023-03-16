package com.project.runningcrew.exception.badinput;

import java.util.Map;

public class YearMonthFormatException extends BadInputException{

    public YearMonthFormatException(int year, int month) {
        super("잘못된 형식의 년, 월 입니다.", Map.of(
                "year", String.valueOf(year),
                "month", String.valueOf(month)));
    }

}
