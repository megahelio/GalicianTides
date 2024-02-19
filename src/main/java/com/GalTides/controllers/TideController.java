package com.GalTides.controllers;

import com.GalTides.entities.Tide;
import com.GalTides.entities.TideDTO;
import com.GalTides.repository.TidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TideController {
    @Autowired
    TidesRepository tidesRepository;

    @GetMapping(path = "/tides")
    public ResponseEntity<TideDTO> findtides(@RequestParam Map<String, String> allargs) {
        try {
            return new ResponseEntity<>(this.tidesRepository.getTides(allargs), HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
