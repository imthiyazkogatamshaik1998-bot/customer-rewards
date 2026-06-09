package com.charter.rewards.service;

import com.charter.rewards.exception.InvalidTransactionException;
import com.charter.rewards.model.CustomerRewardResponse;
import com.charter.rewards.model.MonthlyReward;
import com.charter.rewards.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    @Override
    public List<CustomerRewardResponse> calculateRewards() {

        List<Transaction> transactions = getTransactions();

        Map<Long, List<Transaction>> customerMap =
                transactions.stream()
                        .collect(Collectors.groupingBy(Transaction::getCustomerId));

        List<CustomerRewardResponse> response = new ArrayList<>();

        for (Map.Entry<Long, List<Transaction>> entry : customerMap.entrySet()) {

            Long customerId = entry.getKey();
            List<Transaction> customerTransactions = entry.getValue();

            Map<String, Long> monthlyRewards =
                    customerTransactions.stream()
                            .collect(Collectors.groupingBy(
                                    t -> Month.of(
                                                    t.getTransactionDate().getMonthValue())
                                            .name(),
                                    Collectors.summingLong(
                                            t -> calculatePoints(
                                                    t.getAmount()))));

            List<MonthlyReward> monthList = new ArrayList<>();

            long total = 0;

            for (Map.Entry<String, Long> reward : monthlyRewards.entrySet()) {
                monthList.add(
                        new MonthlyReward(
                                reward.getKey(),
                                reward.getValue()));

                total += reward.getValue();
            }

            response.add(
                    new CustomerRewardResponse(
                            customerId,
                            customerTransactions.get(0).getCustomerName(),
                            monthList,
                            total));
        }

        return response;
    }

    private long calculatePoints(Double amount) {

        if (amount < 0) {
            throw new InvalidTransactionException(
                    "Amount cannot be negative");
        }

        long points = 0;

        if (amount > 100) {
            points += (long) ((amount - 100) * 2);
            points += 50;
        } else if (amount > 50) {
            points += (long) (amount - 50);
        }

        return points;
    }

    private List<Transaction> getTransactions() {

        return Arrays.asList(
                new Transaction(1L, 101L, "Imthiyaz",
                        120.0, java.time.LocalDate.now().minusMonths(2)),

                new Transaction(2L, 101L, "Imthiyaz",
                        75.0, java.time.LocalDate.now().minusMonths(2)),

                new Transaction(3L, 101L, "Imthiyaz",
                        200.0, java.time.LocalDate.now().minusMonths(1)),

                new Transaction(4L, 102L, "Ravi",
                        90.0, java.time.LocalDate.now().minusMonths(1)),

                new Transaction(5L, 102L, "Ravi",
                        130.0, java.time.LocalDate.now())
        );
    }
}