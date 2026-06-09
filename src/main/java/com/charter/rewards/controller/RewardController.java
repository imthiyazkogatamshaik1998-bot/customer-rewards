package com.charter.rewards.controller;

import com.charter.rewards.model.CustomerRewardResponse;
import com.charter.rewards.service.RewardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/api/rewards")
    public List<CustomerRewardResponse> getRewards() {
        return rewardService.calculateRewards();
    }
}