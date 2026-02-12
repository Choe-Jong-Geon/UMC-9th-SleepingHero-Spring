package com.umc_9th.sleepinghero.domain.group.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupInvitationRequest {
    private String groupName;
    private String nickName;
}
