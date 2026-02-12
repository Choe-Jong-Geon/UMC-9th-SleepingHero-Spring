package com.umc_9th.sleepinghero.domain.hero.service;

import com.umc_9th.sleepinghero.domain.sleep.calculator.SleepCalculator;
import com.umc_9th.sleepinghero.domain.hero.converter.HeroConverter;
import com.umc_9th.sleepinghero.domain.hero.dto.req.HeroRequestDTO;
import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.skin.entity.Skin;
import com.umc_9th.sleepinghero.domain.skin.entity.SkinMember;
import com.umc_9th.sleepinghero.domain.skin.exception.SkinErrorCode;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinMemberRepository;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinRepository;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final SkinRepository skinRepository;
    private final MemberRepository memberRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final SkinMemberRepository skinMemberRepository;
    private final SleepCalculator sleepCalculator;


    @Override
    public HeroResponseDTO.HeroDetailDTO getHeroDetail(Long memberId) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));


        return HeroConverter.toHeroDetailDTO(hero);
    }


    @Override
    @Transactional
    public HeroResponseDTO.HeroDetailDTO createDefaultHero(Long memberId) {

        if (heroRepository.findByMemberId(memberId).isPresent()) {
            throw new GeneralException(HeroErrorCode.ALREADY_EXIST_HERO);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));


        List<Long> initialSkinIds = List.of(1L, 2L, 3L);
        List<Skin> initialSkins = skinRepository.findAllById(initialSkinIds);

        if (initialSkins.size() < 3) {
            throw new GeneralException(SkinErrorCode.SKIN_NOT_FOUND);
        }

        initialSkins.forEach(skin -> {
            SkinMember skinMember = SkinMember.builder()
                    .member(member)
                    .skin(skin)
                    .build();
            skinMemberRepository.save(skinMember);
        });

        String finalName = generateUniqueDefaultName();

        Hero newHero = Hero.builder()
                .name(finalName)
                .member(member)
                .currentLevel(1)
                .currentSkin(initialSkins.getFirst())
                .currentExp(0)
                .currentStage(1)
                .build();

        member.updateNickname(finalName);

        Hero savedHero = heroRepository.save(newHero);
        return HeroConverter.toHeroDetailDTO(savedHero);
    }

    @Override
    @Transactional
    public HeroResponseDTO.HeroDetailDTO updateHeroName(Long memberId, HeroRequestDTO.UpdateNameDTO request) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!hero.getName(
        ).equals(request.getName()) && heroRepository.existsByName(request.getName())) {
            throw new GeneralException(HeroErrorCode.DUPLICATE_HERO_NAME);
        }

        hero.updateName(request.getName());

        member.updateNickname(request.getName());

        return HeroConverter.toHeroDetailDTO(hero);
    }

    @Override
    public HeroResponseDTO.SearchHeroResultDTO searchHeroByName(String name) {
        Hero hero = heroRepository.findByName(name)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        Member member = hero.getMember();

        int streak = sleepGoalRepository.findByMemberId(member.getId())
                .map(SleepGoal::getCurrentStreak)
                .orElse(0);

        long totalSeconds = sleepCalculator.calculateTotalSleepSeconds(member);
        int totalHours = sleepCalculator.toHours(totalSeconds);

        return HeroConverter.toSearchHeroResultDTO(hero, streak, totalHours);

    }

    @Override
    @Transactional
    public void checkAndUnlockSkin(Member member, int currentLevel) {

        List<Long> targetSkinIds = null;
        if (currentLevel >= 70) targetSkinIds = List.of(10L, 11L, 12L);
        else if (currentLevel >= 35) targetSkinIds = List.of(7L, 8L, 9L);
        else if (currentLevel >= 10) targetSkinIds = List.of(4L, 5L, 6L);

        if (targetSkinIds != null) {
            for (Long skinId : targetSkinIds) {
                if (!skinMemberRepository.existsByMemberIdAndSkinId(member.getId(), skinId)) {
                    Skin skin = skinRepository.findById(skinId)
                            .orElseThrow(() -> new GeneralException(SkinErrorCode.SKIN_NOT_FOUND));
                    skinMemberRepository.save(SkinMember.builder().member(member).skin(skin).build());
                }
            }
        }
    }

    private String generateUniqueDefaultName() {
        String baseName = "김용사";
        String uniqueName;

        do {
            String suffix = UUID.randomUUID().toString().substring(0, 5);
            uniqueName = baseName + "_" + suffix;
        } while (heroRepository.existsByName(uniqueName)); // 중복이 없을 때까지 반복

        return uniqueName;
    }
}