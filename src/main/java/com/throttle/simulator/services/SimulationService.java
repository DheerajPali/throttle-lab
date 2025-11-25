package com.throttle.simulator.services;

import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.dtos.SimulationResult;
public interface SimulationService {
    SimulationResult runSimulation(SimulationRequest request);
}
