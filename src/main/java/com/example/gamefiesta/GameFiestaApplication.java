package com.example.gamefiesta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GameFiestaApplication{

    public static void main(String[] args) {
        SpringApplication.run(GameFiestaApplication.class, args);

    }


    // Test dzialania dodawania dokumentu do bazy
    // @Bean
    // CommandLineRunner runner(UserRepository repository){
    //     return args -> {
    //         Users user = new Users(
    //             "test",
    //             "123",
    //             "none",
    //             "asd@asd.asd"
    //         );
    //         // dodaje usera
    //         repository.insert(user);
    //     };
        
    // }


    //     @Bean
    // CommandLineRunner runner(TournamentRepository repository){
    //     ArrayList<String> mods = new ArrayList<>();
    //     mods.add("655b309dd80f1b56ddaf0d4e");
    //     return args -> {
    //         Tournament tournament = new Tournament(null, 
    //         "655b309dd80f1b56ddaf0d4", 
    //         mods, 
    //         mods, 
    //         null, 
    //         null, 
    //         null, 
    //         null, 
    //         null
    //         );
    //         repository.insert(tournament);
    //     };
        
    // }

}
