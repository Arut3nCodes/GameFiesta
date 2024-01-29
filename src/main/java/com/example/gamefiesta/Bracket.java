package com.example.gamefiesta;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class Bracket {
    @Id
    private String _id;
    private List<String> listOfMatches;
    private String type;
    public void generateBracket(){

    }
    Bracket(String type){
        this.type = type;
    }
}
