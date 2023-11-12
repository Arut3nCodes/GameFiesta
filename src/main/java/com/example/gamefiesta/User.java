package com.example.gamefiesta;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class User {
    @Id
    private String _id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String permissions;
    @Indexed(unique = true)
    private String email;
    



    public User(String username, String password, String permissions, String email) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.email = email;
    }



}
