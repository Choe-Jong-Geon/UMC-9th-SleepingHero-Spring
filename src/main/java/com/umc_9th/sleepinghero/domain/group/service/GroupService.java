package com.umc_9th.sleepinghero.domain.group.service;

import com.umc_9th.sleepinghero.domain.group.converter.GroupConverter;
import com.umc_9th.sleepinghero.domain.group.dto.req.GroupMakeRequestDto;
import com.umc_9th.sleepinghero.domain.group.dto.req.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.exception.GroupErrorCode;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.group.repository.GroupRepository;
import com.umc_9th.sleepinghero.domain.sleep.Repository.SleepRecordRepository;

import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SleepRecordRepository sleepRecordRepository;

    public String createGroup(GroupMakeRequestDto request, String masterNickName) {

        if (request.getGroupName() == null || request.getDescription() == null) {
            throw new GeneralException(GroupErrorCode.GROUP_NOT_MADE);
        }

        Group newGroup = GroupConverter.toGroup(request, masterNickName);
        groupRepository.save(newGroup);

        return "그룹 생성이 완료되었습니다.";
    }

    public List<GroupRankResponse> getGroupRanking() {
        // 1. 모든 그룹 조회
        List<Group> groups = groupRepository.findAll();

        // 2. 그룹별 평균 수면 시간 미리 계산 (정렬 성능 향상)
        Map<Long, Double> groupSleepAvgMap = groups.stream()
                .collect(Collectors.toMap(
                        Group::getId,
                        group -> calculateGroupSleepAverage(group.getId())
                ));

        // 3. 정렬 로직 (1순위: 인원수, 2순위: 평균 수면시간)
        List<Group> sortedGroups = groups.stream()
                .sorted((g1, g2) -> {
                    // 인원수 내림차순
                    if (g1.getCurrentPeople() != g2.getCurrentPeople()) {
                        return Integer.compare(g2.getCurrentPeople(), g1.getCurrentPeople());
                    }
                    // 평균 수면 시간 내림차순
                    return Double.compare(groupSleepAvgMap.get(g2.getId()), groupSleepAvgMap.get(g1.getId()));
                })
                .collect(Collectors.toList());

        // 4. 리스트 인덱스를 활용해 DTO로 변환 (순위 부여)
        List<GroupRankResponse> result = new ArrayList<>();
        for (int i = 0; i < sortedGroups.size(); i++) {
            Group group = sortedGroups.get(i);
            // i + 1이 곧 순위(rank)가 됩니다.
            result.add(GroupConverter.toGroupRankResponse(group, i + 1));
        }

        return result;
    }

    private double calculateGroupSleepAverage(Long groupId) {
        // GroupMember를 통해 해당 그룹에 속한(ACCEPTED) 멤버들을 가져옴
        List<GroupMember> groupMembers = groupMemberRepository.findAllByHeroGroupsIdAndStatus(groupId, Status.ACCEPTED);

        if (groupMembers.isEmpty()) return 0.0;

        // 멤버들의 수면 기록(success=true)을 합산하여 평균 계산
        return groupMembers.stream()
                .map(GroupMember::getMember)
                .flatMap(member -> sleepRecordRepository.findAllByMemberAndSuccess(member, true).stream())
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).toMinutes())
                .average()
                .orElse(0.0);
    }

}
