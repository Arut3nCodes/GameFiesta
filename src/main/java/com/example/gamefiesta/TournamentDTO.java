package com.example.gamefiesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {
    private String tournamentName;
    private String bracketType;
    private String date;
    private String shortDescription;
    private String description;
}