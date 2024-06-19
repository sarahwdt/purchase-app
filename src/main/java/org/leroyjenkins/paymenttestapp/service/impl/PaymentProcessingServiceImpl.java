package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.dto.PaymentProcessingResult;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessingException;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessorNotRegisteredException;
import org.leroyjenkins.paymenttestapp.service.PaymentProcessingService;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentAdaptersRegistrar;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentProcessorAdapter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentProcessingServiceImpl implements PaymentProcessingService {
    private final PaymentAdaptersRegistrar adaptersRegistrar;

    @Override
    public boolean isPaymentProcessorRegistered(@Nonnull String paymentProcessorName) {
        return adaptersRegistrar.isAdapterRegistered(paymentProcessorName);
    }

    @Override
    public void processPayment(@Nonnull BigDecimal paymentAmount, @Nonnull String taxNumber,
                               @Nonnull String paymentProcessorName) {
        PaymentProcessorAdapter adapter = adaptersRegistrar.getAdapter(paymentProcessorName);
        if (adapter == null) {
            throw new PaymentProcessorNotRegisteredException(paymentProcessorName);
        }
        PaymentProcessingResult paymentResult = adapter.makePayment(paymentAmount);
        if (paymentResult.isSuccessful()) {
            return;
        }

        if (paymentResult.catchedException() != null) {
            throw new PaymentProcessingException(paymentResult.catchedException().getMessage());
        }
        throw new PaymentProcessingException();
    }
}
