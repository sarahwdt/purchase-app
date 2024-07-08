package org.leroyjenkins.paymenttestapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.RoundingMode;

@Configuration
public class SerializationConfiguration {
    @Value("${application.rounding-mode.default}")
    private RoundingMode defaultRoundingMode;

    @Value("${application.serialization.money.scale.default}")
    private int scale;

    @Bean("moneyScale")
    public int scale() {
        return scale;
    }

    @Bean("moneyRoundingMode")
    public RoundingMode roundingMode() {
        return defaultRoundingMode;
    }
}
