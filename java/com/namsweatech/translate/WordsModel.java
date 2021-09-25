package com.namsweatech.translate;

public class WordsModel {

    String word;
    String description;
    String fromTribe;
    String toTribe;

    public WordsModel() {

    }

    public WordsModel(String word, String description, String fromTribe, String toTribe) {
        this.word = word;
        this.description = description;
        this.fromTribe = fromTribe;
        this.toTribe = toTribe;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromTribe() {
        return fromTribe;
    }

    public void setFromTribe(String fromTribe) {
        this.fromTribe = fromTribe;
    }

    public String getToTribe() {
        return toTribe;
    }

    public void setToTribe(String toTribe) {
        this.toTribe = toTribe;
    }
}
