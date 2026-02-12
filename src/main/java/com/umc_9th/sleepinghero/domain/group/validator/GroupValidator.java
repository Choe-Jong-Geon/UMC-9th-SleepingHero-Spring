package com.umc_9th.sleepinghero.domain.group.validator;

import com.umc_9th.sleepinghero.domain.group.dto.req.GroupMakeRequestDto;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.exception.GroupErrorCode;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupValidator {

    private final GroupMemberRepository groupMemberRepository;

    public void validateGroupRequest(GroupMakeRequestDto request) {
        if (request.getGroupName() == null || request.getDescription() == null) {
            throw new GeneralException(GroupErrorCode.GROUP_NOT_MADE);
        }
    }

    public void validateMasterAuthority(Group group, String nickname) {
        if (!group.getMaster().equals(nickname)) {
            throw new GeneralException(GroupErrorCode.NOT_GROUP_MASTER);
        }
    }

    public void validateGroupCapacity(Group group) {
        if (group.getCurrentPeople() >= group.getMaxPeople()) {
            throw new GeneralException(GroupErrorCode.GROUP_FULL);
        }
    }

    public void validateInvitationEligibility(Member invitee, Group group) {
        if (groupMemberRepository.existsByMemberAndHeroGroups(invitee, group)) {
            throw new GeneralException(MemberErrorCode.FRIEND_ALREADY_EXISTS);
        }
        validateGroupCapacity(group);
    }

    public void validateNotSelfKick(String admin, String target) {
        if (admin.equals(target)) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }
    }

    public void validateNotMasterLeave(Group group, String nickname) {
        if (group.getMaster().equals(nickname)) {
            throw new GeneralException(GroupErrorCode.MASTER_NOT_EXITED);
        }
    }

    public void validateDeletableCondition(Group group) {
        if (group.getCurrentPeople() > 1) {
            throw new GeneralException(GroupErrorCode.GROUP_NOT_DELETED);
        }
    }
}