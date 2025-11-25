package com.throttle.simulator.dtos;

import com.throttle.simulator.models.constants.StrategyType;
import com.throttle.simulator.models.constants.TrafficProfile;
import com.throttle.simulator.strategy.RateLimitStrategy;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SimulationRequest {

    @NotNull
    @Min(1)
    private Integer requestsPerSecond;

    @NotNull
    private StrategyType strategy;

    @NotNull
    @Min(1)
    private Integer durationSeconds;

    // Token Bucket / Leaky Bucket specific
    private Integer bucketCapacity;        // optional for strategies that don't use it
    private Integer refillRatePerSecond;   // optional for strategies that don't use it

    // Fixed Window / Sliding Window specific
    private Integer windowSizeSeconds;     // optional for strategies that don't use it

    //for traffic profile modification
//    private TrafficProfile trafficProfile = TrafficProfile.CONSTANT;
    private TrafficProfile trafficProfile;
    private Integer durationInSeconds = 10;
    private Integer periodicIntervalInSeconds;
}
