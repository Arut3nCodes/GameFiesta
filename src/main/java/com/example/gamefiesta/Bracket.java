package com.example.gamefiesta;

import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public abstract class Bracket {
    @Id
    private String _id;
    private List<String> listOfMatches;
    private String type;
    public void generateBracket(){

    }
}
