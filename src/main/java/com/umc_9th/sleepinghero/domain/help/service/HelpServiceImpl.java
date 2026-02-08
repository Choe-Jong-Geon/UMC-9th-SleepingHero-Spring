package com.umc_9th.sleepinghero.domain.help.service;


import com.umc_9th.sleepinghero.domain.help.dto.req.HelpRequestDTO;
import com.umc_9th.sleepinghero.domain.help.dto.res.HelpResponseDTO;
import com.umc_9th.sleepinghero.domain.help.entity.Help;
import com.umc_9th.sleepinghero.domain.help.exception.HelpErrorCode;
import com.umc_9th.sleepinghero.domain.help.repository.HelpRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class HelpServiceImpl implements HelpService {

    private final HelpRepository helpRepository;
    private final MemberRepository memberRepository;
//    private final RestTemplate restTemplate = new RestTemplate();


//    @Value("${discord.webhook.url}") // application.yml에 등록한 디스코드 주소
//    private String discordWebhookUrl;

    @Override
    public void createHelpInquiry(Long memberId, HelpRequestDTO.CreateHelpDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        Help help = Help.builder()
                .member(member)
                .type(request.getType())
                .content(request.getContent())
                .responseEmail(request.getResponseEmail())
                .build();

        helpRepository.save(help);

//        sendDiscordNotification(help);
    }

//    private void sendDiscordNotification(Help help) {
//        try {
//            Map<String, Object> body = new HashMap<>();
//            String message = String.format(
//                    "📢 **[신규 문의 접수]**\n- **유형**: %s\n- **작성자**: %s\n- **내용**: %s\n- **회신 이메일**: %s",
//                    help.getType().name(),
//                    help.getMember().getNickName(),
//                    help.getContent(),
//                    help.getResponseEmail() != null ? help.getResponseEmail() : "없음"
//            );
//            body.put("content", message);
//            restTemplate.postForEntity(discordWebhookUrl, body, String.class);
//        } catch (Exception e) {
//            System.err.println("디스코드 알림 전송 실패: " + e.getMessage());
//        }
//    }
}

