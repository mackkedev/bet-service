package com.mk.bettinggenerator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WinProbability {
    private int homeWin;
    private int draw;
    private int awayWin;
}
