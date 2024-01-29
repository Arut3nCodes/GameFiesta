package com.example.gamefiesta;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bracket {
    @Id
    protected String _id;
    protected List<String> listOfMatches;
    @Transient
    protected List<Match> listOfMatchObjects;
    protected String type;
    public void generateLadder(ArrayList<String> listOfTeams){

    }

    public void generateRandomLadder(ArrayList<String> listOfTeams){

    }

    Bracket(String type){
        this.listOfMatches = new ArrayList<>();
        this.type = type;
    }
}
