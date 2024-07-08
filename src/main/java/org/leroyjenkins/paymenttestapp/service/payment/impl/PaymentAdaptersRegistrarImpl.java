package org.leroyjenkins.paymenttestapp.service.payment.impl;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.ApplicationBootstrapError;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentAdaptersRegistrar;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentProcessorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class PaymentAdaptersRegistrarImpl implements PaymentAdaptersRegistrar {
    private final Map<String, PaymentProcessorAdapter> processorAdapters = new HashMap<>();

    @Autowired
    // Autowire all PaymentProcessorAdapters and store it
    public PaymentAdaptersRegistrarImpl(Set<PaymentProcessorAdapter> adapters) {
        collectAdapters(adapters);
    }

    protected void collectAdapters(Set<PaymentProcessorAdapter> adapters) {
        for (PaymentProcessorAdapter adapter : adapters) {
            if (processorAdapters.containsKey(adapter.getProcessorName())) {
                // Fail-fast because we can't decide which processor is more prioritized
                throw new ApplicationBootstrapError("Duplicate payment processor adapter name: "
                        + adapter.getProcessorName());
            }
            processorAdapters.put(adapter.getProcessorName(), adapter);
        }
    }

    @Override
    public PaymentProcessorAdapter getAdapter(@Nonnull String paymentProcessorName) {
        return processorAdapters.get(paymentProcessorName);
    }

    @Override
    public boolean isAdapterRegistered(@Nonnull String paymentProcessorName) {
        return processorAdapters.containsKey(paymentProcessorName);
    }
}
