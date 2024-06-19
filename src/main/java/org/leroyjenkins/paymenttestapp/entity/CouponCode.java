package org.leroyjenkins.paymenttestapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "coupon_codes")
@Getter
@Setter
@NoArgsConstructor
public class CouponCode {
    @Id
    private String code;

    @Column
    private BigDecimal percent;

    @Column
    private BigDecimal fixed;

    public boolean isFixed() {
        return fixed != null;
    }
}
