package com.example.avalancheLabs.services;

import com.example.avalancheLabs.controllers.APIController;
import com.example.avalancheLabs.models.Data;
import com.example.avalancheLabs.models.DataCached;
import com.example.avalancheLabs.models.DataInTO;
import com.example.avalancheLabs.repositories.DataCacheRepository;
import com.example.avalancheLabs.repositories.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DataService {

    @Autowired
    DataRepository dataRepository;

    @Autowired
    DataCacheRepository dataCacheRepository;

    private static Logger logger = LoggerFactory.getLogger(APIController.class);

    // Create an entry in h2 and Cache
    public ResponseEntity postData(DataInTO data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created = LocalDateTime.parse(data.getCreated(), formatter);
        Data result = null;

        try {
            result = dataRepository.save(
                    new Data(
                            data.getForeign_id(),
                            data.getWord(),
                            created
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }

        if(Objects.equals(result.getForeign_id(), data.getForeign_id())) {

            DataCached test = dataCacheRepository.save(
                    new DataCached(
                            result.getForeign_id(),
                            result.getWord(),
                            result.getCreated()
                    )
            );

            if(Objects.equals(result.getForeign_id(), test.getId()))
                return ResponseEntity.ok("data saved!");
        }
        return ResponseEntity.status(400).build();
    }

    // Get all entries from h2
    public ResponseEntity<List<Data>> getAll() {
        List<Data> exampleList = new ArrayList<>();
        Iterator<Data> data = dataRepository.findAll().iterator();
        while(data.hasNext()) {
            // ExampleData example = data.iterator().next();
            exampleList.add(data.next());
        }
        return ResponseEntity.ok(exampleList);
    }

    // Get all entries from cache
    public ResponseEntity<List<DataInTO>> getAllCached() {
        List<DataInTO> exampleList = new ArrayList<>();
        Iterator<DataCached> data = dataCacheRepository.findAll().iterator();
        while(data.hasNext()) {
            // ExampleData example = data.iterator().next();
            DataCached cached = data.next();
            exampleList.add( new DataInTO(
                    cached.getId(),
                    cached.getWord(),
                    cached.getCreated().toString()
            ));
        }
        return ResponseEntity.ok(exampleList);
    }

    // Get single entry from cache
    public ResponseEntity<DataInTO> getCached(int foreign_id) {
        Optional<DataCached> result = dataCacheRepository.findById(foreign_id);

        if(result.isEmpty())
            return ResponseEntity.notFound().build();

        DataCached data = result.get();

        return ResponseEntity.ok(new DataInTO(
                data.getId(),
                data.getWord(),
                data.getCreated().toString()
        ));
    }

    // Remove all entries from cache
    public ResponseEntity<String> flush() {
        try {
            dataCacheRepository.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Data removed from cache");
    }

    // Check if a given word possibly matches single entry in cached word list
    public ResponseEntity<Boolean> posiblySame(String word) {

        Iterator<DataCached> iterator = dataCacheRepository.findAll().iterator();
        List<String> wordlist = new ArrayList<>();
        boolean result = false;

        while (iterator.hasNext()) {
            DataCached cached = iterator.next();
            wordlist.add( cached.getWord());
        }

        // Return false if there is no entries in cache
        if(wordlist == null || wordlist.size() == 0)
            ResponseEntity.ok(false);

        // Loop through word list stored in cache
        for(String cachedWord: wordlist) {

            // If there will be an error we will set it to false
            boolean areSame = true;

            // Check maybe the words are equal
            if(Objects.equals(word, cachedWord)) {
                break;
            }

            // if words have different length, simply continue,
            // you need to set false here because this could be the last word
            if( word.length() != cachedWord.length() ) {
                areSame = false;
                continue;
            }

            // Loop through all charecters
            // The idea is that if this loops with no issue, that means that the words arfe possibly the same
            for (int i = 0; i < word.length(); i++){
                char c = word.charAt(i);
                char cc = cachedWord.charAt(i);

                // if the chars are different and the char is a letter, this means the words are not the same
                if(c != cc && Character.isLetter(c)) {
                    areSame = false;
                    break;
                }
            }
            // If a word passed all checks consider it a possibly the same and break from the loop
            if(areSame) {
                result = true;
                break;
            }
        }

        return ResponseEntity.ok(result);
    }
}
