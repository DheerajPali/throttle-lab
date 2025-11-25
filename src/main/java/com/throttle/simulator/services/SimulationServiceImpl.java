package com.throttle.simulator.services;

import com.throttle.simulator.factory.StrategyFactory;
import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.dtos.SimulationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SimulationServiceImpl implements SimulationService {

    private final StrategyFactory strategyFactory;

    @Override
    public SimulationResult runSimulation(SimulationRequest request) {
        return strategyFactory.getStrategy(request)
                .simulate(request);
    }
}

