package com.mk.bettinggenerator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Prediction {
    int game;
    String predictionBet;
    String hedging; //gardering


    @JsonProperty(value = "result")
    public String getResult() {
        hedging = hedging == null ? "" : hedging;
        return predictionBet + hedging;
    }

    public void randomizeGardering(List<String> possibleValues) {
        Random random = new Random();
        String newPrediction = possibleValues.get(random.nextInt(3));

        if(predictionBet.equals(newPrediction)) {
            randomizeGardering(possibleValues);
        } else {
            hedging = newPrediction;
        }
    }
}
