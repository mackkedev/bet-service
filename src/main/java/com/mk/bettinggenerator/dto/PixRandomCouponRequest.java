package com.mk.bettinggenerator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PixRandomCouponRequest {
    private int games;
    private int halfGardering;
    private int coupons;
}
