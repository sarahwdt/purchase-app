package org.leroyjenkins.paymenttestapp.unit.service.payment;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.exception.ApplicationBootstrapError;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentProcessorAdapter;
import org.leroyjenkins.paymenttestapp.service.payment.impl.PaymentAdaptersRegistrarImpl;
import org.leroyjenkins.paymenttestapp.unit.service.AbstractUnitTest;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class PaymentAdaptersRegistrarImplTest extends AbstractUnitTest {
    private final String paymentProcessor1 = "paymentProcessor1";
    private final String paymentProcessor2 = "paymentProcessor2";
    @Mock
    private PaymentProcessorAdapter mockAdapter1 = Mockito.mock(PaymentProcessorAdapter.class);
    @Mock
    private PaymentProcessorAdapter mockAdapter2 = Mockito.mock(PaymentProcessorAdapter.class);

    private final PaymentAdaptersRegistrarImpl paymentAdaptersRegistrar;

    {
        when(mockAdapter1.getProcessorName()).thenReturn(paymentProcessor1);
        when(mockAdapter2.getProcessorName()).thenReturn(paymentProcessor2);
        paymentAdaptersRegistrar = new PaymentAdaptersRegistrarImpl(Set.of(mockAdapter1, mockAdapter2));
    }

    @Test
    void Should_ReturnTrue_When_isAdapterRegisteredCalledAndAdapterIsRegistered() {
        boolean expectedResult = true;

        boolean actualResult = paymentAdaptersRegistrar.isAdapterRegistered(paymentProcessor1);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void Should_ReturnFalse_When_isAdapterRegisteredCalledAndAdapterIsNotRegistered() {
        boolean expectedResult = false;

        boolean actualResult = paymentAdaptersRegistrar.isAdapterRegistered("unregisteredProcessor");

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void Should_ReturnAccordingAdapter_When_isAdapterRegisteredCalledAndAdapterIsRegistered() {
        PaymentProcessorAdapter actualAdapter = paymentAdaptersRegistrar.getAdapter(paymentProcessor2);

        assertThat(actualAdapter.getProcessorName()).isEqualTo(paymentProcessor2);
    }

    @Test
    void Should_ReturnNull_When_isAdapterRegisteredCalledAndAdapterIsNotRegistered() {
        PaymentProcessorAdapter actualAdapter = paymentAdaptersRegistrar.getAdapter("unregisteredProcessor");

        assertThat(actualAdapter).isNull();
    }

    @Test
    void Should_ThrowException_When_TwoAdaptersWithSameNameWereRegistered() {
        when(mockAdapter1.getProcessorName()).thenReturn(paymentProcessor1);
        when(mockAdapter2.getProcessorName()).thenReturn(paymentProcessor1);
        Set<PaymentProcessorAdapter> adapters = Set.of(mockAdapter1, mockAdapter2);

        assertThatThrownBy(() -> new PaymentAdaptersRegistrarImpl(adapters))
                .isInstanceOf(ApplicationBootstrapError.class)
                .hasMessageContaining(paymentProcessor1)
                .hasMessageContaining("Duplicate payment processor adapter name");
    }
}