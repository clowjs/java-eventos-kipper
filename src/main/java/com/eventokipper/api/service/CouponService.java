package com.eventokipper.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventokipper.api.domain.coupon.Coupon;
import com.eventokipper.api.domain.coupon.CouponRequestDTO;
import com.eventokipper.api.repositories.CouponRepository;

@Service
public class CouponService {

  @Autowired
  private CouponRepository couponRepository;

  public Coupon createCoupon(CouponRequestDTO data) {
    Coupon newCoupon = new Coupon();
    newCoupon.setCode(data.code());
    newCoupon.setDiscount(data.discount());
    newCoupon.setValid(data.valid());
    newCoupon.setEvent(data.eventId());

    couponRepository.save(newCoupon);

    return newCoupon;
  }
}
