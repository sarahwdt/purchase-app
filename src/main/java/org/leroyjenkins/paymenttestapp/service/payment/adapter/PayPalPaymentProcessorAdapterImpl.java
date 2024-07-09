package org.leroyjenkins.paymenttestapp.service.payment.adapter;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.leroyjenkins.paymenttestapp.dto.PaymentProcessingResult;
import org.leroyjenkins.paymenttestapp.exception.PayPalPaymentException;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentProcessorAdapter;
import org.leroyjenkins.paymenttestapp.service.payment.processor.PayPalPaymentProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class PayPalPaymentProcessorAdapterImpl implements PaymentProcessorAdapter {
    private final PayPalPaymentProcessor payPalPaymentProcessor;
    private final RoundingMode roundingMode;

    public PayPalPaymentProcessorAdapterImpl(PayPalPaymentProcessor payPalPaymentProcessor,
                                             @Qualifier("mathContextRoundingMode") RoundingMode roundingMode) {
        this.payPalPaymentProcessor = payPalPaymentProcessor;
        this.roundingMode = roundingMode;
    }

    @Override
    @Nonnull
    public String getProcessorName() {
        return "paypal";
    }

    @Override
    @Nonnull
    public PaymentProcessingResult makePayment(@Nonnull BigDecimal paymentAmount) {
        Integer paymentAmountInt = paymentAmount.movePointRight(2).intValue();
        try {
            payPalPaymentProcessor.makePayment(paymentAmountInt);
            return PaymentProcessingResult.builder().isSuccessful(true).build();
        } catch (PayPalPaymentException e) {
            log.error("Failed to make purchase via '{}' payment processor, payment amount: '{}'",
                    getProcessorName(), paymentAmount.setScale(2, roundingMode), e);
            return PaymentProcessingResult.builder().isSuccessful(false).catchedException(e).build();
        }
    }
}
