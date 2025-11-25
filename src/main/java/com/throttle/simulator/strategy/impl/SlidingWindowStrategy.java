package com.throttle.simulator.strategy.impl;

import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.dtos.SimulationResult;
import com.throttle.simulator.strategy.RateLimitStrategy;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowStrategy implements RateLimitStrategy {

    private final int limitPerWindow;
    private final int windowSizeSeconds;
    private final Queue<Long> requestTimestamps = new LinkedList<>();

    public SlidingWindowStrategy(int limitPerWindow, int windowSizeSeconds) {
        this.limitPerWindow = limitPerWindow;
        this.windowSizeSeconds = windowSizeSeconds;
    }

    @Override
    public SimulationResult simulate(SimulationRequest request) {
        // Use same logic as token bucket simulation loop
        int totalRequests = request.getRequestsPerSecond() * request.getDurationSeconds();
        int allowed = 0;
        int rejected = 0;

        long start = System.currentTimeMillis();

        for (int second = 0; second < request.getDurationSeconds(); second++) {
            for (int r = 0; r < request.getRequestsPerSecond(); r++) {
                if (allowRequest()) allowed++;
                else rejected++;
            }
        }

        long end = System.currentTimeMillis();
        double avgProcessingTime = (end - start) / (double) totalRequests;

        return SimulationResult.builder()
                .totalRequests(totalRequests)
                .allowedRequests(allowed)
                .rejectedRequests(rejected)
                .successRatePercent(allowed * 100.0 / totalRequests)
                .avgProcessingTimeMs(avgProcessingTime)
                .build();
    }

    @Override
    public boolean allowRequest() {
        refill();  // Clean up expired timestamps

        if (requestTimestamps.size() < limitPerWindow) {
            requestTimestamps.add(System.currentTimeMillis());
            return true;
        } else {
            return false;
        }
    }


    public void refill() {
        long now = System.currentTimeMillis();
        long windowStart = now - windowSizeSeconds * 1000L;

        // Remove timestamps outside the sliding window
        while (!requestTimestamps.isEmpty() && requestTimestamps.peek() < windowStart) {
            requestTimestamps.poll();
        }
    }

}

