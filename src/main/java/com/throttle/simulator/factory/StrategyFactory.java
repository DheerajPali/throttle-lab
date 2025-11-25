package com.throttle.simulator.factory;

import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.models.FixedWindowRequest;
import com.throttle.simulator.models.LeakyBucketRequest;
import com.throttle.simulator.strategy.RateLimitStrategy;
import com.throttle.simulator.strategy.impl.FixedWindowStrategy;
import com.throttle.simulator.strategy.impl.LeakyBucketStrategy;
import com.throttle.simulator.strategy.impl.SlidingWindowStrategy;
import com.throttle.simulator.strategy.impl.TokenBucketStrategy;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {
    public RateLimitStrategy getStrategy(SimulationRequest request) {
        return (RateLimitStrategy) switch (request.getStrategy()) {
            case TOKEN_BUCKET -> new TokenBucketStrategy(request.getBucketCapacity(), request.getRefillRatePerSecond(), request.getBucketCapacity());
            case FIXED_WINDOW -> new FixedWindowStrategy(request.getBucketCapacity(), request.getWindowSizeSeconds());
            case SLIDING_WINDOW -> new SlidingWindowStrategy(request.getBucketCapacity(), request.getWindowSizeSeconds());
            case LEAKY_BUCKET -> new LeakyBucketStrategy(request.getBucketCapacity(), request.getRefillRatePerSecond());
            default -> throw new IllegalArgumentException("Unsupported strategy type: " + request.getStrategy());
        };
    }

}
