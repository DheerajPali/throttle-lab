package com.throttle.simulator.strategy;

import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.dtos.SimulationResult;

public interface RateLimitStrategy {
    SimulationResult simulate(SimulationRequest request);
    boolean allowRequest();
    void refill();
}
