package com.example.avalancheLabs.models;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private int foreign_id;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private LocalDateTime created;

    public Data() { }
    public Data(int foreign_id, String word, LocalDateTime created) {
        this.foreign_id = foreign_id;
        this.word = word;
        this.created = created;
    }

    public UUID getId() { return id; }
    public int getForeign_id() {
        return foreign_id;
    }
    public String getWord() {
        return word;
    }
    public LocalDateTime getCreated() {
        return created;
    }

    public void setForeign_id(int foreign_id) {
        this.foreign_id = foreign_id;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
