package com.example.avalancheLabs.controllers;

import com.example.avalancheLabs.models.Data;
import com.example.avalancheLabs.models.DataInTO;
import com.example.avalancheLabs.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    DataService dataService;

    @PostMapping("/migration/ocr")
    public ResponseEntity postData(@Valid @RequestBody DataInTO data) {
        return dataService.postData(data);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Data>> getAll() {
        return dataService.getAll();
    }

    @GetMapping("/cached/all")
    public ResponseEntity<List<DataInTO>> getAllCached() {
        return dataService.getAllCached();
    }
    @GetMapping("/cached/details/{foreign_id}")
    public ResponseEntity<DataInTO> getCached(@Valid @PathVariable int foreign_id) {
        return dataService.getCached(foreign_id);
    }

    @GetMapping("/cached/flush")
    public ResponseEntity<String> flush() {
        return dataService.flush();
    }

    @GetMapping("/cached/possibly-same")
    public ResponseEntity posiblySame(@Valid @RequestParam("word") String word) {
        return dataService.posiblySame(word);
    }
}