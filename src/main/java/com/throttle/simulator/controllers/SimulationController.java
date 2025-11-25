package com.throttlelab.simulator.controllers;

import com.throttlelab.simulator.models.SimulationRequest;
import com.throttlelab.simulator.models.SimulationResult;
import com.throttlelab.simulator.services.SimulationService;
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
