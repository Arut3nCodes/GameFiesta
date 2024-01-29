package com.example.gamefiesta;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bracket {
    @Id
    protected String _id;
    protected List<String> listOfMatches;
    protected String type;
    public void generateBracket(){

    }
    Bracket(String type){
        this.listOfMatches = new ArrayList<>();
        this.type = type;
    }
}
