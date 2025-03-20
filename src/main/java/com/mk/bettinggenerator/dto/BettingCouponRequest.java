package com.mk.bettinggenerator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingCouponRequest {
    private List<GamePredictionDto> gamePredictions;
}
