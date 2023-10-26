package com.GalTides;

import com.GalTides.controllers.TideController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class GalicianTidesApplication implements CommandLineRunner {
    @Autowired
    TideController tideController;

    public static void main(String[] args) {
        SpringApplication.run(GalicianTidesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, String> allargs = new HashMap<>();
        allargs.put("idPorto", "7");
        allargs.put("data", "23/02/2024");
        System.out.println(tideController.findtides(allargs).toString());
    }
}