package com.eventokipper.api.domain.coupon;

import java.util.UUID;

public record CouponRequestDTO(String code, Integer discount, Long valid, UUID eventId) {

}