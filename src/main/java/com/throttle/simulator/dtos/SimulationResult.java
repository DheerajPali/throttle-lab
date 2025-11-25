package com.throttle.simulator.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationResult {
    private long totalRequests;
    private long allowedRequests;
    private long rejectedRequests;
    private double successRatePercent;
    private double avgProcessingTimeMs;
}
