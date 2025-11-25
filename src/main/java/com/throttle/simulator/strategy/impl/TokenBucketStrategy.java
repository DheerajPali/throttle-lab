package com.throttle.simulator.strategy.impl;

import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.dtos.SimulationResult;
import com.throttle.simulator.strategy.RateLimitStrategy;

public class TokenBucketStrategy implements RateLimitStrategy {

    private final int bucketCapacity;
    private final int refillRatePerSecond;
    private int currentTokens;


    public TokenBucketStrategy(int bucketCapacity, int refillRatePerSecond, int currentTokens) {
        this.bucketCapacity = bucketCapacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.currentTokens = currentTokens;
    }

    @Override
    public SimulationResult simulate(SimulationRequest request) {

        int totalRequests = request.getRequestsPerSecond() * request.getDurationSeconds();
        int allowed = 0;
        int rejected = 0;

        currentTokens = bucketCapacity;

        long start = System.currentTimeMillis();

        for (int second = 0; second < request.getDurationSeconds(); second++) {
            refill();

            for (int r = 0; r < request.getRequestsPerSecond(); r++) {
                if (allowRequest()) {
                    allowed++;
                } else {
                    rejected++;
                }
            }
        }

        long end = System.currentTimeMillis();
        double totalTimeMs = end - start;
        double avgProcessingTime = totalTimeMs / totalRequests;

        return SimulationResult.builder()
                .totalRequests(totalRequests)
                .allowedRequests(allowed)
                .rejectedRequests(rejected)
                .successRatePercent((allowed * 100.0) / totalRequests)
                .avgProcessingTimeMs(avgProcessingTime)
                .build();
    }

    @Override
    public boolean allowRequest() {
        if (currentTokens > 0) {
            currentTokens--;
            return true;
        }
        return false;
    }

    @Override
    public void refill() {
        currentTokens = Math.min(bucketCapacity, currentTokens + refillRatePerSecond);
    }
}
