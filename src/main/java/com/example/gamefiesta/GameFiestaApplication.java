package com.example.gamefiesta;


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
    //         User user = new User(
    //             "test",
    //             "123",
    //             "none",
    //             "asd@asd.asd"
    //         );
    //         // dodaje usera
    //         repository.insert(user);
    //     };
        
    // }


}
