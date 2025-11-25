package com.throttlelab.simulator.strategy.impl;

import com.throttlelab.simulator.models.SimulationRequest;
import com.throttlelab.simulator.models.SimulationResult;
import com.throttlelab.simulator.strategy.RateLimitStrategy;

public class LeakyBucketStrategy implements RateLimitStrategy {

    private final int bucketCapacity;
    private final int leakRatePerSecond;
    private int currentLoad;

    public LeakyBucketStrategy(int bucketCapacity, int leakRatePerSecond) {
        this.bucketCapacity = bucketCapacity;
        this.leakRatePerSecond = leakRatePerSecond;
        this.currentLoad = 0;
    }

    @Override
    public SimulationResult simulate(SimulationRequest request) {

        int totalRequests = request.getRequestsPerSecond() * request.getDurationSeconds();
        int allowed = 0;
        int rejected = 0;

        currentLoad = 0;  // reset

        long start = System.currentTimeMillis();

        for (int second = 0; second < request.getDurationSeconds(); second++) {

            refill(); // leak the bucket

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
        if (currentLoad < bucketCapacity) {
            currentLoad++;
            return true;
        }
        return false;
    }

    @Override
    public void refill() {
        currentLoad = Math.max(0, currentLoad - leakRatePerSecond);
    }
}
