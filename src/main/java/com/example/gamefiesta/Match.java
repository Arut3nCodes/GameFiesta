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
    public Match(String _id){
            this._id = _id;
            this.teamA = "TBD";
            this.teamB = "TBD";
            this.scoreA = 0;
            this.scoreB = 0;
            this.matchWinner = "TBD";
            this.setStatus(0);
    }

    public void setStatus(int choice) {
        switch(choice){
            case 0:
                this.status = "NOT_STARTED";
                break;
            case 1:
                this.status = "IN_PROGRESS";
                break;
            case 2:
                this.status = "FINISHED";
                break;
        }
    }

    @Override
    public String toString(){
        if(this.getStatus().equals("NOT_STARTED")) {
            return "Match " + get_id() + ": " + getTeamA() + " vs " + getTeamB() + " | Status: " + getStatus() + "| Winner: "+ getMatchWinner();
        }
        else{
            return "Match " + get_id() + ": " + getTeamA() + " vs " + getTeamB() + " | Status: " + getStatus() + " | Score: " + getScoreA() + "-" + getScoreB() + " | Winner: " + getMatchWinner();
        }
    }

    public void setVictor(boolean choice){
        if(getStatus().equals("IN_PROGRESS")){
            if (choice) {
                setMatchWinner(getTeamA());
            } else {
                setMatchWinner(getTeamB());
            }
            this.setStatus(2);
        }
    }

    public boolean autoSetVictor() {
        if (getStatus().equals("IN_PROGRESS")) {
            if (getScoreA() > getScoreB()) {
                setMatchWinner(getTeamA());
                setStatus(2);
                return true;
            } else if (getScoreA() < getScoreB()) {
                setMatchWinner(getTeamB());
                setStatus(2);
                return true;
            }
        }
        if (("BYE".equals(getTeamA()) || "BYE".equals(getTeamB())) && (!"TBD".equals(getTeamA()) || !"TBD".equals(getTeamB()))) {
            setMatchWinner(getTeamA().equals("BYE") ? getTeamB() : getTeamA());
            setStatus(2);
            return true;
        }
        return false;
    }

    public boolean startMatch(){
        if(getStatus().equals("NOT_STARTED") && !teamA.equals("TBD") && !teamB.equals("TBD")){
            setStatus(1);
            return true;
        }
        return false;
    }

    public void addPointToTeamA(){
        this.scoreA++;
    }
    public void addPointToTeamB() {
        this.scoreB++;
    }
}

