package com.example.gamefiesta;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Tournament {
    @Id
    private String _id;
    private String organizer;
    private List<String> listOfMods;
    private List<String> listOfTeams;
    private List<List<String>> listOfSquads;
    private List<String> listOfPrices;
    //Bracket bracket;
    private Bracket bracket;
    private Date date;
    private String status;
}
