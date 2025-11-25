package com.throttlelab.simulator.strategy.impl;

import com.throttlelab.simulator.models.SimulationRequest;
import com.throttlelab.simulator.models.SimulationResult;
import com.throttlelab.simulator.strategy.RateLimitStrategy;

public class FixedWindowStrategy implements RateLimitStrategy {

//    private final int limitPerSecond;
    private int currentCount;
    private long windowStartTimestamp;
    private final int limitPerWindow;
    private final int windowSizeSeconds;

    public FixedWindowStrategy(int limitPerWindow, int windowSizeSeconds) {
        this.limitPerWindow = limitPerWindow;
        this.windowSizeSeconds = windowSizeSeconds;
        this.currentCount = 0;
        this.windowStartTimestamp = System.currentTimeMillis();
    }


    @Override
    public SimulationResult simulate(SimulationRequest request) {

        int totalRequests = request.getRequestsPerSecond() * request.getDurationSeconds();
        int allowed = 0;
        int rejected = 0;

        long start = System.currentTimeMillis();

        for (int second = 0; second < request.getDurationSeconds(); second++) {

            if (second % windowSizeSeconds == 0) {
                currentCount = 0;
            }

            for (int r = 0; r < request.getRequestsPerSecond(); r++) {
                if (allowRequest()) {
                    allowed++;
                } else {
                    rejected++;
                }
            }
        }

        long end = System.currentTimeMillis();
        double avgProcessingTime = (end - start) / (double) totalRequests;

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
        long now = System.currentTimeMillis();

        if (now - windowStartTimestamp >= 1000) {
            refill();
            windowStartTimestamp = now;
        }

        if (currentCount < limitPerWindow) {
            currentCount++;
            return true;
        }
        return false;
    }

    @Override
    public void refill() {
        currentCount = 0;
    }

}
