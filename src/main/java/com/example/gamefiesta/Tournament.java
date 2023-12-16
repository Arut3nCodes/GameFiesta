package com.example.gamefiesta;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
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
    private String title;
    private String shortDescription;
    private String Description;
    private List<String> invitedList;
}
