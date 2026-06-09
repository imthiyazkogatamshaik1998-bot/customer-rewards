package com.charter.rewards.service;

import com.charter.rewards.model.CustomerRewardResponse;

import java.util.List;

public interface RewardService {

    List<CustomerRewardResponse> calculateRewards();
}