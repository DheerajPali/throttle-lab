package com.throttle.simulator.services;

import com.throttle.simulator.dtos.SimulationResult;
import com.throttle.simulator.models.constants.TrafficProfile;
import com.throttle.simulator.strategy.RateLimitStrategy;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TrafficGeneratorServiceImpl implements TrafficGeneratorService {

    private final Random random = new Random();

    @Override
    public SimulationResult generateTraffic(RateLimitStrategy strategy,
                                            int requestsPerSecond,
                                            int durationSeconds,
                                            TrafficProfile profile) {

        int allowed = 0;
        int rejected = 0;
        long totalProcessingTimeMs = 0;

        int totalRequests = requestsPerSecond * durationSeconds;

        // Pre-compute requests per second distribution
        int[] requestsPerSecondArray = calculateRequestsDistribution(profile, requestsPerSecond, durationSeconds, totalRequests);

        for (int second = 0; second < durationSeconds; second++) {

            strategy.refill();

            int requestsThisSecond = requestsPerSecondArray[second];

            for (int i = 0; i < requestsThisSecond; i++) {
//                strategy.refill();
                long startTime = System.currentTimeMillis();

                if (strategy.allowRequest()) {
                    allowed++;
                    // Simulate processing time (e.g., random 1-10ms)
                    long processingTime = 1 + random.nextInt(10);
                    totalProcessingTimeMs += processingTime;
                } else {
                    rejected++;
                    // Optional: small processing time for rejected requests
                    long processingTime = 1 + random.nextInt(3);
                    totalProcessingTimeMs += processingTime;
                }

                // Optional: real sleep to simulate RPS can be added if needed
            }
        }

        double avgProcessingTimeMs = totalRequests > 0 ? ((double) totalProcessingTimeMs / totalRequests) : 0;

        return SimulationResult.builder()
                .totalRequests(totalRequests)
                .allowedRequests(allowed)
                .rejectedRequests(rejected)
                .successRatePercent((allowed * 100.0) / totalRequests)
                .avgProcessingTimeMs(avgProcessingTimeMs)
                .build();
    }

    private int[] calculateRequestsDistribution(TrafficProfile profile,
                                                int baseRps,
                                                int duration,
                                                int totalRequests) {

        int[] distribution = new int[duration];

        switch (profile) {
            case CONSTANT -> {
                int rps = totalRequests / duration;
                for (int i = 0; i < duration; i++) distribution[i] = rps;
                distribution[duration - 1] += totalRequests - rps * duration;
            }

            case RAMP_UP -> {
                int sum = 0;
                for (int i = 0; i < duration; i++) {
                    distribution[i] = (int) Math.round((totalRequests * (i + 1.0)) / (duration * (duration + 1) / 2));
                }
                int assigned = 0;
                for (int req : distribution) assigned += req;
                distribution[duration - 1] += totalRequests - assigned;
            }

                case SPIKE -> {
                    if (duration == 1) {
                        distribution[0] = totalRequests; // All requests in the single second
                    } else {
                        distribution[0] = (int) (totalRequests * 0.6);
                        int remaining = totalRequests - distribution[0];
                        for (int i = 1; i < duration; i++) {
                            distribution[i] = remaining / (duration - 1);
                        }
                        distribution[duration - 1] += remaining % (duration - 1);
                    }
                }


            case RANDOM -> {
                int remaining = totalRequests;
                for (int i = 0; i < duration - 1; i++) {
                    int r = random.nextInt(remaining + 1);
                    distribution[i] = r;
                    remaining -= r;
                }
                distribution[duration - 1] = remaining;
            }

            case PERIODIC -> {
                int high = totalRequests / duration;
                for (int i = 0; i < duration; i++) {
                    distribution[i] = (i % 2 == 0) ? (high + high / 2) : (high / 2);
                }
                int sum = 0;
                for (int req : distribution) sum += req;
                distribution[duration - 1] += totalRequests - sum;
            }
        }

        return distribution;
    }
}
