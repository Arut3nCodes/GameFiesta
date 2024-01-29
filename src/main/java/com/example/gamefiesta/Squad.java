package com.example.gamefiesta;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
public class Squad {
    @Id
    private String _id;
    private String team;
    private List<String> players;

}
