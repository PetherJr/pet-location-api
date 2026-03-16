package com.petherson.petlocation.infrastructure.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter endpointCallCounter(MeterRegistry registry) {
        return Counter.builder("pet_location_requests_total")
                .description("Total number of requests to the pet location endpoint")
                .register(registry);
    }

    @Bean
    public Timer geocodingTimer(MeterRegistry registry) {
        return Timer.builder("pet_location_geocoding_latency")
                .description("Time taken for reverse geocoding")
                .register(registry);
    }

    @Bean
    public Counter geocodingFailureCounter(MeterRegistry registry) {
        return Counter.builder("pet_location_geocoding_failures_total")
                .description("Total number of geocoding failures")
                .register(registry);
    }
}
