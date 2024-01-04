package com.example.gamefiesta;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Document
@AllArgsConstructor
public class Team {
    @Id
    private String _id;
    private String leader;
    @Indexed(unique = true)
    private String name;
    private List<String> players;
    private List<String> invitedList;
}
