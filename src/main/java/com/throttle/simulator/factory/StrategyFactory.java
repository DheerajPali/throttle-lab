package com.throttlelab.simulator.factory;

import com.throttlelab.simulator.models.SimulationRequest;
import com.throttlelab.simulator.strategy.RateLimitStrategy;
import com.throttlelab.simulator.strategy.impl.FixedWindowStrategy;
import com.throttlelab.simulator.strategy.impl.LeakyBucketStrategy;
import com.throttlelab.simulator.strategy.impl.SlidingWindowStrategy;
import com.throttlelab.simulator.strategy.impl.TokenBucketStrategy;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {

    public RateLimitStrategy getStrategy(SimulationRequest request) {
        return switch (request.getStrategy()) {
            case TOKEN_BUCKET -> new TokenBucketStrategy(request.getBucketCapacity(), request.getRefillRatePerSecond(), request.getBucketCapacity());
            case FIXED_WINDOW -> new FixedWindowStrategy(request.getBucketCapacity(), request.getWindowSizeSeconds());
            case SLIDING_WINDOW -> new SlidingWindowStrategy(request.getBucketCapacity(), request.getWindowSizeSeconds());
            case LEAKY_BUCKET -> new LeakyBucketStrategy(request.getBucketCapacity(), request.getRefillRatePerSecond());
            default -> throw new IllegalArgumentException("Unsupported strategy type: " + request.getStrategy());
        };
    }

}
