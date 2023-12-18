package com.example.gamefiesta;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    private String _id;
    private String teamA;
    private String teamB;
    private int scoreA;
    private int scoreB;
    private String matchWinner;
    private String status;
    private Date date;
    // przerobić żeby była lista "spotkań", spotkanie: TeamA, TeamB, Status, Data, a Match ma tylko tą liste i ID
    public Match(String _id){
        this._id = _id;
        this.teamA = "TBD";
        this.teamB = "TBD";
        this.scoreA = 0;
        this.scoreB = 0;
        this.matchWinner = "TBD";
        this.status = "NOT STARTED";
    }

    @Override
    public String toString(){
        if(this.getStatus().equals("NOT STARTED")) {
            return "Match " + get_id() + ": " + getTeamA() + " vs " + getTeamB() + " | Status: " + getStatus() + getMatchWinner();
        }
        else{
            return "Match " + get_id() + ": " + getTeamA() + " vs " + getTeamB() + " | Status: " + getStatus() + " | Score: " + getScoreA() + "-" + getScoreB() + " | Winner: " + getMatchWinner();
        }
    }

    public String setVictor(){
        return null;
    }

    public void addPointToTeamA(){
        this.scoreA++;
    }
    public void addPointToTeamB() {
        this.scoreB++;
    }
}

