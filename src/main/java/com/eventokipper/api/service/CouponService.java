package com.eventokipper.api.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventokipper.api.domain.coupon.Coupon;
import com.eventokipper.api.domain.coupon.CouponRequestDTO;
import com.eventokipper.api.domain.event.Event;
import com.eventokipper.api.repositories.CouponRepository;
import com.eventokipper.api.repositories.EventRepository;

@Service
public class CouponService {

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private EventRepository eventRepository;

  public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO data) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("Event not found"));

    Coupon newCoupon = new Coupon();

    newCoupon.setCode(data.code());
    newCoupon.setDiscount(data.discount());
    newCoupon.setValid(new Date(data.valid()));
    newCoupon.setEvent(event);

    return couponRepository.save(newCoupon);
  }
}
