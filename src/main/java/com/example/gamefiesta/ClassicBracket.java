package com.example.gamefiesta;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
@Data
public class ClassicBracket extends Bracket{
    @Transient
    ArrayList<Match> listOfMatchObjects;
    public ClassicBracket(){
        super("classic");
        this.setListOfMatchObjects(new ArrayList<Match>());
    }

    public ArrayList<Match> getListOfMatchObjects() {
        return listOfMatchObjects;
    }

    public void setListOfMatchObjects(ArrayList<Match> listOfMatchObjects) {
        this.listOfMatchObjects = listOfMatchObjects;
    }

    //Na ten moment wersja bez bye'ów
    //Budujemy drzewo binarne w liście, także
    //Na ten moment bracket będzie przechowywany w bracketcie, pomijam bd
    public void generateLadder(ArrayList<String> listOfTeams){
        if(isPowerOfTwo(listOfTeams.size())) {
            int sizeOfList = listOfTeams.size()-1;
            for (int i = 0; i < sizeOfList; i++) {

                listOfMatchObjects.add(new Match(Integer.toString(i)));
                this.getListOfMatches().add(this.get_id() + i);

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

    public void printLadder(){
        if(!listOfMatchObjects.isEmpty()) {
            for (Match m : listOfMatchObjects) {
                System.out.println(m.toString());
            }
        }
        else{
            System.out.println("There are no matches to print");
        }
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

}

