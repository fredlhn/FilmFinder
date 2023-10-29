package com.sydney.filmfinder.Class;

public class UserInput {
    private String words;

    public UserInput(String words) {
        this.words = words;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return words;
    }
}

