package com.project.runningcrew.exception.badinput;

import java.util.Map;

public class YearMonthFormatException extends BadInputException {

    public YearMonthFormatException(int year, int month) {
        super(BadInputErrorCode.YEAR_MONTH_FORMAT, Map.of(
                "year", String.valueOf(year),
                "month", String.valueOf(month)));
    }

}
