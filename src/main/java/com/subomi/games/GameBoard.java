package com.subomi.games;

import java.util.List;
import java.util.UUID;

public class GameBoard {
    private String phrase;

    public String getPhrase() {
        return this.phrase;
    }

    public void generatePhrase() {
        UUID uniqueId = UUID.randomUUID();

        this.phrase = "I am a phrase " + uniqueId.toString();
    }

    public List<Integer> considerGuess(char c) {
        return null;
    }
}
