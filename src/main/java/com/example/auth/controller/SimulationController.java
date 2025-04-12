package com.example.auth.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sim")

public class SimulationController {
    private final Counter testCounter;

    public SimulationController(MeterRegistry registry) {
        this.testCounter = Counter.builder("test_counter").register(registry);
    }

    @GetMapping("/hello")
    public String hello() {
        testCounter.increment();
        return "Hello world";
    }
}
