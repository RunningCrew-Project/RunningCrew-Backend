package com.project.runningcrew.blocked.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetBlockedInfoListResponse {

    private List<SimpleBlockedInfoDto> content = new ArrayList<>();

}
