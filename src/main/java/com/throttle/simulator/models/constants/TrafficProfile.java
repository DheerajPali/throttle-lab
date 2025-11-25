package com.throttle.simulator.models.constants;

public enum TrafficProfile {
    CONSTANT,       // same requests per second
    SPIKE,          // huge burst at start
    RAMP_UP,        // gradually increases load
    RANDOM,         // unpredictable load
    PERIODIC        // burst every X seconds
}
