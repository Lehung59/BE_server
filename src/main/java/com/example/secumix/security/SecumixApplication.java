package com.example.secumix.security;



import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class SecumixApplication implements CommandLineRunner {




    public static void main(String[] args) {
        SpringApplication.run(SecumixApplication.class, args);
//        "C:\Program Files\Google\Chrome\Application\chrome.exe" --disable-web-security --disable-gpu --user-data-dir=%LOCALAPPDATA%\Google\chromeTemp"


    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Runnnn...");
    }
}
