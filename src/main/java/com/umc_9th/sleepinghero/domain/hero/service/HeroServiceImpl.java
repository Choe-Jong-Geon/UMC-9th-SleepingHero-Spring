package com.umc_9th.sleepinghero.domain.hero.service;

import com.umc_9th.sleepinghero.domain.hero.dto.req.HeroRequestDTO;
import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.hero.util.LevelPolicy;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.skin.entity.Skin;
import com.umc_9th.sleepinghero.domain.skin.exception.SkinErrorCode;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinRepository;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final SkinRepository skinRepository;
    private final MemberRepository memberRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final SleepRecordRepository sleepRecordRepository;


    @Override
    public HeroResponseDTO.HeroDetailDTO getHeroDetail(Long memberId) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));


        return HeroResponseDTO.HeroDetailDTO.builder()
                .heroId(hero.getId())
                .name(hero.getName())
                .currentLevel(hero.getCurrentLevel())
                .currentExp(hero.getCurrentExp())
                .needExp(LevelPolicy.needExp(hero.getCurrentLevel()))
                .currentStage(hero.getCurrentStage())
                .build();
    }


    @Override
    @Transactional
    public HeroResponseDTO.HeroDetailDTO createDefaultHero(Long memberId) {

        if (heroRepository.findByMemberId(memberId).isPresent()) {
            throw new GeneralException(HeroErrorCode.ALREADY_EXIST_HERO);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));


        Skin defaultSkin = skinRepository.findById(1L)
                .orElseThrow(() -> new GeneralException(SkinErrorCode.SKIN_NOT_FOUND));

        String finalName = generateUniqueDefaultName();

        Hero newHero = Hero.builder()
                .name(finalName)
                .member(member)
                .currentLevel(1)
                .currentSkin(defaultSkin)
                .currentExp(0)
                .currentStage(1)
                .build();

        Hero savedHero = heroRepository.save(newHero);
        return HeroResponseDTO.toDetailDTO(savedHero);
    }

    @Override
    @Transactional
    public HeroResponseDTO.HeroDetailDTO updateHeroName(Long memberId, HeroRequestDTO.UpdateNameDTO request) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!hero.getName().equals(request.getName()) && heroRepository.existsByName(request.getName())) {
            throw new GeneralException(HeroErrorCode.DUPLICATE_HERO_NAME);
        }

        hero.updateName(request.getName());

        member.updateNickname(request.getName());

        return HeroResponseDTO.toDetailDTO(hero);
    }

    @Override
    public HeroResponseDTO.SearchHeroResultDTO searchHeroByName(String name) {
        Hero hero = heroRepository.findByName(name)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        Member member = hero.getMember();

        int streak = sleepGoalRepository.findByMemberId(member.getId())
                .map(SleepGoal::getCurrentStreak)
                .orElse(0);

        long totalSeconds = calculateTotalSleepSeconds(member);
        int totalHours = (int) (totalSeconds / 3600);

        return HeroResponseDTO.SearchHeroResultDTO.builder()
                .memberId(member.getId())
                .heroId(hero.getId())
                .heroName(hero.getName())
                .level(hero.getCurrentLevel())
                .skinId(hero.getCurrentSkin().getId())
                .continuousSleepDays(streak)
                .totalSleepHour(totalHours)
                .build();
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


    // ------------------------------ 계산 로직 ------------------------------

    private Long calculateTotalSleepSeconds(Member member) {
        return sleepRecordRepository.findAllByMemberAndIsSuccess(member, true).stream()
                .filter(sr -> sr.getSleptTime() != null && sr.getWokeTime() != null)
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).getSeconds())
                .sum();
    }
}