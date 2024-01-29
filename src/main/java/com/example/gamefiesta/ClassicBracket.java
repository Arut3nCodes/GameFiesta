package com.example.gamefiesta;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collections;

@Data
public class ClassicBracket extends Bracket{
    @Transient
    ArrayList<Match> listOfMatchObjects;
    ArrayList<String> mapOfAdvancement;
    public ClassicBracket(){
        super("classic");
        this.setListOfMatchObjects(new ArrayList<>());
        this.setMapOfAdvancement(new ArrayList<>());
    }
    //Na ten moment wersja bez bye'ów
    //Budujemy drzewo binarne w liście, także
    //Na ten moment bracket będzie przechowywany w bracketcie, pomijam bd
    @Override
    public void generateLadder(ArrayList<String> listOfTeams){
        if(isPowerOfTwo(listOfTeams.size())) {
            int sizeOfList = listOfTeams.size()-1;
            for (int i = 0; i < sizeOfList; i++) {
                listOfMatchObjects.add(new Match(Integer.toString(i)));
                this.getListOfMatches().add(this.get_id() + i);
                //Operacje na map of advancement
                if(i+1 <= sizeOfList/2) {
                    this.getMapOfAdvancement().add(this.get_id() + i);
                    this.getMapOfAdvancement().add(this.get_id() + i);
                }
                //Dodawanie do bracketu liniowo
                if (i >= sizeOfList / 2){
                    int index = (i - sizeOfList/2) % ((sizeOfList+1)/2) * 2;
                    this.getListOfMatchObjects().get(i).setTeamA(listOfTeams.get(index));
                    this.getListOfMatchObjects().get(i).setTeamB(listOfTeams.get(index+1));
                }
            }
        }
        else{
            System.out.println("There is not enough teams to form a bracket");
        }
    }
    @Override
    public void generateRandomLadder(ArrayList<String> listOfTeams) {
        if (listOfTeams.size() == 0) {
            System.out.println("No teams provided.");
            return;
        }

        int nearestPowerOfTwo = 1;
        while (nearestPowerOfTwo < listOfTeams.size()) {
            nearestPowerOfTwo *= 2;
        }

        if (listOfTeams.size() != nearestPowerOfTwo) {
            int numOfByes = nearestPowerOfTwo - listOfTeams.size();
            for (int i = 0; i < numOfByes; i++) {
                listOfTeams.add("BYE");
            }
        }

        Collections.shuffle(listOfTeams);

        int sizeOfList = listOfTeams.size() - 1;
        for (int i = 0; i < sizeOfList; i++) {
            listOfMatchObjects.add(new Match(this.get_id() + i));
            this.getListOfMatches().add(this.get_id() + i);
            if(i+1 <= sizeOfList/2) {
                this.getMapOfAdvancement().add(this.get_id() + i);
                this.getMapOfAdvancement().add(this.get_id() + i);
            }
            if (i >= sizeOfList / 2) {
                int index = (i - sizeOfList / 2) % ((sizeOfList + 1) / 2) * 2;
                this.getListOfMatchObjects().get(i).setTeamA(listOfTeams.get(index));
                this.getListOfMatchObjects().get(i).setTeamB(listOfTeams.get(index + 1));
            }
        }
    }
    public void printLadder(){
        System.out.println("----------------");
        if(!listOfMatchObjects.isEmpty()) {
            for (Match m : listOfMatchObjects) {
                System.out.println(m.toString());
            }
        }
        else{
            System.out.println("There are no matches to print");
        }
        System.out.println("----------------");
    }

    private boolean isPowerOfTwo(int n)
    {
        if (n == 0)
            return false;

        return ((int)Math.ceil(Math.log(n) / Math.log(2)) == (int)Math.floor(Math.log(n) / Math.log(2)));
    }

    public Match getMatch(String matchId){
        for(Match obj: getListOfMatchObjects()){
            if(obj.get_id().equals(matchId)) {
                System.out.println("Match found");
                return obj;
            }
        }
        System.out.println("Match with id: " + matchId + " was not found");
        return null;
    }

    //Todo: Id meczu to powinna byc skladowa turnieju i id meczu(wazne)
    //Todo: Might not be safe/work for finals

    public boolean startMatch(int matchIndex){
        if(matchIndex < getListOfMatches().size() & matchIndex >= 0) {
            Match match = getListOfMatchObjects().get(matchIndex);
            if (!match.getTeamA().equals("TBD") && !match.getTeamB().equals("TBD")){
                if(match.getStatus().equals("NOT_STARTED")){
                    System.out.println("Match " + matchIndex + "was started");
                    match.setStatus(1);
                    return true;
                }
                else{
                    System.out.println("Match was already started!");
                    return false;
                }
            }
            else{
                System.out.println("One of the teams wasn't determined yet");
                return false;
            }
        }
        return false;
    }
    public boolean manualAdvanceTeam(String matchId, boolean whichTeam){
        Match match = this.getMatch(matchId);
        if(match != null){
            match.setVictor(whichTeam);
            Match toBeAdvancedTo = this.getListOfMatchObjects().stream()
                    .filter(m -> m.get_id().equals(this.mapOfAdvancement.get(this.getListOfMatchObjects().indexOf(match)-1)))
                    .findFirst()
                    .orElse(null);
            if(this.getListOfMatchObjects().indexOf(match) % 2 == 0) {
                toBeAdvancedTo.setTeamB(match.getMatchWinner());
            }
            else{
                toBeAdvancedTo.setTeamA(match.getMatchWinner());
            }
            return true;
        }
        return false;
    }

    public boolean manualAdvanceTeam(int matchIndex, boolean whichTeam) {
        Match match = getListOfMatchObjects().get(matchIndex);
        if (match != null) {
            match.setVictor(whichTeam);
            Match toBeAdvancedTo = this.getListOfMatchObjects().stream()
                    .filter(m -> m.get_id().equals(this.mapOfAdvancement.get(this.getListOfMatchObjects().indexOf(match)-1)))
                    .findFirst()
                    .orElse(null);
            if (this.getListOfMatchObjects().indexOf(match) % 2 == 0) {
                toBeAdvancedTo.setTeamB(match.getMatchWinner());
            } else {
                toBeAdvancedTo.setTeamA(match.getMatchWinner());
            }
            return true;
        }
        return false;
    }

    public boolean autoAdvanceTeam(int matchIndex){
        Match match = getListOfMatchObjects().get(matchIndex);
        if(match.autoSetVictor()){
            return true;
        }
        return false;
    }

}

