package com.eventokipper.api.controllers;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventokipper.api.domain.coupon.Coupon;
import com.eventokipper.api.domain.coupon.CouponRequestDTO;
import com.eventokipper.api.service.CouponService;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

  @Autowired
  private CouponService couponService;

  public ResponseEntity<Coupon> create(@RequestParam("code") String code,
      @RequestParam("discount") Integer discount,
      @RequestParam("valid") Date valid,
      @RequestParam("event_id") UUID event_id) {
    CouponRequestDTO couponRequestDTO = new CouponRequestDTO(code, discount, valid, event_id);
    Coupon newCoupon = this.couponService.createCoupon(couponRequestDTO);

    return ResponseEntity.ok(newCoupon);
  }
}