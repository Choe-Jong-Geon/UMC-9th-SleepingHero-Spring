package com.umc_9th.sleepinghero.domain.help.service;


import com.umc_9th.sleepinghero.domain.help.dto.res.HelpResponseDTO;
import com.umc_9th.sleepinghero.domain.help.exception.HelpErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelpServiceImpl implements HelpService {

    @Value("${help.inquiry-url}")
    private String inquiryUrl;

    @Override
    public HelpResponseDTO.InquiryUrlDTO getInquiryUrl() {

        if (inquiryUrl == null || inquiryUrl.isBlank()) {
            throw new GeneralException(HelpErrorCode.INQUIRY_URL_NOT_CONFIGURED);
        }
        return HelpResponseDTO.InquiryUrlDTO.builder()
                .url(inquiryUrl)
                .build();
    }
}

