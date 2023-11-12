package com.example.gamefiesta;

import java.util.List;

import lombok.Data;
@Data
public abstract class Bracket {
    private String _id;
    private List<String> listOfMatches;
    private String type;
}
