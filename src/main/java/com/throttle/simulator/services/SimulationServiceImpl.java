package com.throttle.simulator.services;

import com.throttle.simulator.factory.StrategyFactory;
import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.dtos.SimulationResult;
import com.throttle.simulator.strategy.RateLimitStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SimulationServiceImpl implements SimulationService {

    private final StrategyFactory strategyFactory;
    private final TrafficGeneratorService trafficGeneratorService;

    @Override
    public SimulationResult runSimulation(SimulationRequest request) {
        RateLimitStrategy strategy = strategyFactory.getStrategy(request);

//        int totalRequests = request.getRequestsPerSecond() * request.getDurationSeconds();

        return trafficGeneratorService.generateTraffic(
                strategy,
                request.getRequestsPerSecond(),
                request.getDurationSeconds(),
                request.getTrafficProfile()
        );

//        return strategy.simulate(request);
    }
}