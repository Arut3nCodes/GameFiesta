package com.example.gamefiesta;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Team {
    @Id
    private String _id;
    private String leader;
    private String name;
    private List<String> players;
}
