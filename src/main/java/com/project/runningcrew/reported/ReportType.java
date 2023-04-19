package com.project.runningcrew.reported;

import com.project.runningcrew.exception.notFound.PostTypeNotFoundException;

import java.util.Arrays;

public enum ReportType {

    PRIVACY("privacy"),
    ABUSE("abuse"),
    ADVERTISING("advertising"),
    PAPERING("papering"),
    PORNOGRAPHY("pornography"),
    COPYRIGHT("copyright"),
    ETC("etc");


    public String value;
    ReportType(String value) { this.value = value; }
    public String getValue() { return value; }

    public static ReportType getReportType(String value) {
        return Arrays.stream(ReportType.values())
                .filter(p -> value.equals(p.getValue()))
                .findAny()
                .orElseThrow(PostTypeNotFoundException::new);
    }

}
