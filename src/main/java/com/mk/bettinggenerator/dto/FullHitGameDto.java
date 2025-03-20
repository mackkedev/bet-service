package com.mk.bettinggenerator.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullHitGameDto {
    private int gameIndex;
    private List<FullHitGamePrediction> goalPercentages;
}
