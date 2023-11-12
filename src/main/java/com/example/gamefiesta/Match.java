package com.example.gamefiesta;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Document
@Data
public class Match {
    @Id
    private String _id;
    private String teamA;
    private String teamB;
    private String status;
    private Date date;
    // przerobić żeby była lista "spotkań", spotkanie: TeamA, TeamB, Status, Data, a Match ma tylko tą liste i ID
}
