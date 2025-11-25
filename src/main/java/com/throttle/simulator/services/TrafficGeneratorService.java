package com.throttle.simulator.services;

import com.throttle.simulator.dtos.SimulationResult;
import com.throttle.simulator.models.constants.TrafficProfile;
import com.throttle.simulator.strategy.RateLimitStrategy;

public interface TrafficGeneratorService {
    SimulationResult generateTraffic(RateLimitStrategy strategy,
                                     int totalRequests,
                                     int durationSeconds,
                                     TrafficProfile profile);
}
