package com.umc_9th.sleepinghero.domain.home.controller;

import com.umc_9th.sleepinghero.domain.home.dto.res.DashBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/dashboard")
    public ResponseEntity<DashBoardResponse> dashboard() {
        return ResponseEntity.ok(new DashBoardResponse(
                1L, 1,1,0));
    }
}
