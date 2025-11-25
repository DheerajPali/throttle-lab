package com.throttlelab.simulator.strategy;

import com.throttlelab.simulator.models.SimulationRequest;
import com.throttlelab.simulator.models.SimulationResult;

public interface RateLimitStrategy {
    SimulationResult simulate(SimulationRequest request);

    boolean allowRequest();
    void refill();
}
