package org.leroyjenkins.paymenttestapp.unit;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leroyjenkins.paymenttestapp.exception.BusinessLogicException;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractUnitTest {

    protected void assertThatThrownByIsTheSameAs(
            ThrowableAssert.ThrowingCallable callable,
            BusinessLogicException throwable
    ) {
        assertThatThrownBy(callable)
                .isInstanceOf(throwable.getClass())
                .hasMessageContaining(throwable.getMessage());
    }
}
