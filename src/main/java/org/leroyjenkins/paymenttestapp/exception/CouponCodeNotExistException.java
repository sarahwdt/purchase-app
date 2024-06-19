package org.leroyjenkins.paymenttestapp.exception;

import java.util.Map;

public class CouponCodeNotExistException extends BusinessLogicException {
    private static final String ERROR_MESSAGE = "Coupon code not exist";

    public CouponCodeNotExistException(String couponCode) {
        super(ERROR_MESSAGE, String.format("'%s': %s", couponCode, ERROR_MESSAGE),
                Map.of("couponCode", couponCode));
    }
}
