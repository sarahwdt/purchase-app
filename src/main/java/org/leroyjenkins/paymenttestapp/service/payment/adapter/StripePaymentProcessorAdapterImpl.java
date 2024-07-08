package org.leroyjenkins.paymenttestapp.service.payment.adapter;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.leroyjenkins.paymenttestapp.dto.PaymentProcessingResult;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentProcessorAdapter;
import org.leroyjenkins.paymenttestapp.service.payment.processor.StripePaymentProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class StripePaymentProcessorAdapterImpl implements PaymentProcessorAdapter {
    private final StripePaymentProcessor stripePaymentProcessor;
    private final RoundingMode roundingMode;

    public StripePaymentProcessorAdapterImpl(StripePaymentProcessor stripePaymentProcessor,
                                             @Qualifier("mathContextRoundingMode") RoundingMode roundingMode) {
        this.stripePaymentProcessor = stripePaymentProcessor;
        this.roundingMode = roundingMode;
    }

    @Override
    @Nonnull
    public String getProcessorName() {
        return "stripe";
    }

    @Override
    @Nonnull
    public PaymentProcessingResult makePayment(@Nonnull BigDecimal paymentAmount) {
        float paymentAmountFloat = paymentAmount.floatValue();
        boolean isSuccessful = stripePaymentProcessor.pay(paymentAmountFloat);
        if (!isSuccessful) {
            log.error("Failed to make purchase via '{}' payment processor, payment amount: '{}'",
                    getProcessorName(), paymentAmount.setScale(2, roundingMode));
        }
        return PaymentProcessingResult.builder().isSuccessful(isSuccessful).build();
    }
}
