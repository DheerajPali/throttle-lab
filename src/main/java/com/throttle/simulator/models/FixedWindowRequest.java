package com.throttle.simulator.models;

import com.throttle.simulator.dtos.SimulationRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
public class FixedWindowRequest extends SimulationRequest {

    @NotNull
    @Min(1)
    private Integer limitPerWindow;

    @NotNull
    @Min(1)
    private Integer windowSizeSeconds;
}
