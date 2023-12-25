package com.example.gamefiesta;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Inbox {
    private String type;
    private String source;
    private String destination;
}
