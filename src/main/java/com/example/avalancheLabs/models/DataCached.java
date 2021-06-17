package com.example.avalancheLabs.models;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@RedisHash("ExampleData")
public class DataCached implements Serializable {

    private int id;

    private  String word;

    private LocalDateTime created;

    public DataCached() { }
    public DataCached(int id , String word, LocalDateTime created) {
        this.id = id;
        this.word = word;
        this.created = created;
    }

    public int getId() { return id; }
    public String getWord() {
        return word;
    }
    public LocalDateTime getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

}
