package com.example.gamefiesta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.UUID;

@Data
@Document(collection="tournament")
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    @Id
    private String _id;
    private String organizer;
    private List<String> listOfMods;
    private List<String> listOfTeams;
    private List<List<String>> listOfSquads;
    private List<String> listOfPrices;
    private Bracket bracket;
    private Date date;
    private String status;
    @Indexed(unique=true)
    private String title;
    private String shortDescription;
    private String description;
    private List<String> invitedList;

    public Tournament(String tournamentName, String bracketType, Date date, String shortDescription, String description){
        this.organizer = "organizer";
        this.listOfMods = new ArrayList<>();
        this.listOfTeams = new ArrayList<>();
        this.listOfSquads = new ArrayList<>();
        this.listOfPrices = new ArrayList<>();
        this.bracket = new Bracket(bracketType);
        this.date = date;
        this.status = "";
        this.title = tournamentName;
        this.shortDescription = shortDescription;
        this.description = description;
        this.invitedList = new ArrayList<>();
    }
}
