package org.leroyjenkins.paymenttestapp.configuration.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneySerializer extends JsonSerializer<BigDecimal> {
    private final int scale;
    private final RoundingMode roundingMode;

    public MoneySerializer(@Qualifier("moneyScale") int scale,
                           @Qualifier("moneyRoundingMode") RoundingMode roundingMode) {
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeNumber(bigDecimal.setScale(scale, roundingMode));
    }
}
