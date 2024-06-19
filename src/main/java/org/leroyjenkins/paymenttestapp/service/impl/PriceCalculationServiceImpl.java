package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.entity.Product;
import org.leroyjenkins.paymenttestapp.exception.EntityNotFoundException;
import org.leroyjenkins.paymenttestapp.repository.ProductRepository;
import org.leroyjenkins.paymenttestapp.service.CouponCodeService;
import org.leroyjenkins.paymenttestapp.service.PriceCalculationService;
import org.leroyjenkins.paymenttestapp.service.TaxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PriceCalculationServiceImpl implements PriceCalculationService {
    private final ProductRepository productRepository;
    private final CouponCodeService couponCodeService;
    private final TaxService taxService;


    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculatePrice(int productId, @Nonnull String taxNumber, String couponCode) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", productId));
        BigDecimal price = product.getPrice();
        price = taxService.applyTax(price, taxNumber);
        if (couponCode != null) {
            price = couponCodeService.applyCouponCode(price, couponCode);
        }

        return price;
    }
}
