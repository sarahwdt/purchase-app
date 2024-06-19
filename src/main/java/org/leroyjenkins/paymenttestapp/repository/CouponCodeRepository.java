package org.leroyjenkins.paymenttestapp.repository;

import org.leroyjenkins.paymenttestapp.entity.CouponCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponCodeRepository extends JpaRepository<CouponCode, String> {
}
