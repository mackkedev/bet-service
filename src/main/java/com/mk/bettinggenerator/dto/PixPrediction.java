package com.mk.bettinggenerator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PixPrediction {
    int game;
    String gameName;
    List<String> predictions;
}
