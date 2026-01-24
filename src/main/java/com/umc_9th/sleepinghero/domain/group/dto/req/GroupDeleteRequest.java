package com.umc_9th.sleepinghero.domain.group.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDeleteRequest {
    private String groupName;
}
