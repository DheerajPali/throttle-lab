package com.throttle.simulator.controllers;

import com.throttle.simulator.dtos.SimulationRequest;
import com.throttle.simulator.dtos.SimulationResult;
import com.throttle.simulator.services.SimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulator")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/run")
    public ResponseEntity<SimulationResult> runRateLimitSimulation(
            @Valid @RequestBody SimulationRequest request) {

        SimulationResult response = simulationService.runSimulation(request);
        return ResponseEntity.ok(response);
    }
}
