package com.umc_9th.sleepinghero.domain.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponse {

    private Long memberId;
    private String nickname;
    private String profilePicture;

}
