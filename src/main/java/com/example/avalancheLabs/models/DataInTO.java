package com.example.avalancheLabs.models;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class DataInTO {

    private int foreign_id;

    private String word;

    private String created;

    public DataInTO() { }
    public DataInTO(int foreign_id, String word, String created) {
        this.foreign_id = foreign_id;
        this.word = word;
        this.created = created;
    }

    public int getForeign_id() {
        return foreign_id;
    }
    public String getWord() {
        return word;
    }
    public String getCreated() {
        return created;
    }

    public void setForeign_id(int foreign_id) {
        this.foreign_id = foreign_id;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public void setCreated(String created) {
        this.created = created;
    }

}
