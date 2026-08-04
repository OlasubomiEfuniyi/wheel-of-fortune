package com.subomi.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GameBoard {
    private char[] phrase;
    private HashMap<Character, List<Integer>> charPositions;
    private HashSet<Character> guessedChars;

    public String getPhrase() {
        return new String(this.phrase);
    }

    /**
     * Generates a phrase based on a category, or a random phrase if no category is provided.
     * The phrase will not contain any punctuation mark. It may contain spaces between words
     * but will not start or end with a space. The phrase will only contain letters of the
     * english alphabet.
     */
    public void generatePhrase(String category) {
        UUID uniqueId = UUID.randomUUID();

        this.phrase = ("I am a phrase " + uniqueId.toString()).toLowerCase().toCharArray();
        this.charPositions = new HashMap<>();
        this.guessedChars = new HashSet<>();

        // Map each char in the phrase to a list of positions where it occurs
        for(int i = 0; i < this.phrase.length; i++) {
            char c = this.phrase[i];

            if (!Character.isAlphabetic(c)) {
                continue;
            }

            if(!charPositions.containsKey(c)) {
                charPositions.put(c, new ArrayList<>());
            }

            charPositions.get(c).add(i);
        }
    }

    /** 
     * Check if a guessed letter is pressent in the phrase. A guess is case insensitive.
     * @param guess The guessed character or phrase.
     * @return A GuessResult object with information about the outcome of the guess.
     */
    public GuessResult considerGuess(String guess) {
        if (guess == null) {
            return null;
        }

        guess = guess.toLowerCase().trim();

        if (guess.length() == 1 && !Character.isAlphabetic(guess.charAt(0)))
        {
            return GuessResult.invalidGuess(guess, true, "Non alphabetic character");
        }
        else if (guess.length() > 1 && guess.length() < this.phrase.length) {
            return GuessResult.invalidGuess(guess, false, "Guess length should be either one or the length of the phrase"); // Invalid guess
        }

        if (guess.length() == 1) {
            char c = guess.charAt(0);
            if (charPositions.containsKey(c) && !guessedChars.contains(c)) {
                // Correct one char guess
                guessedChars.add(c);
                if (isPhraseGuessed()) {
                    return GuessResult.validCorrectOneCharGuessThatCompletes(guess, List.copyOf(charPositions.get(c)));
                } 
                else {
                    return GuessResult.validCorrectOneCharGuess(guess, List.copyOf(charPositions.get(c)));
                }
            }
            else {
                return GuessResult.validIncorrectOneCharGuess(guess, null); // Incorrect one char guess.
            }
        }
        else {
            if (!guess.matches("^[a-z]+( [a-z]+)*$")) {
                return GuessResult.invalidGuess(guess, false, "Guess contains invalid characters or unexpected spaces");
            }
            if (guess.equals(new String(this.phrase))) {
                //Mark all characters as guessed
                List<Integer> newlyGuessedPositions = markAllCharsAsGuessed();
                return GuessResult.validCorrectWholePhraseGuess(guess, newlyGuessedPositions); // Correct whole phrase guess
            }
            else {
                return GuessResult.validIncorrectWholePhraseGuess(guess, null); // Incorrect whole phrase guess.
            }
        }
    }

    public void clear() {
        this.phrase = null;
    }

    private List<Integer> markAllCharsAsGuessed() {
        ArrayList<Integer> result = new ArrayList<>();

        for (Character c: this.phrase) {
            if (!Character.isSpaceChar(c) && !this.guessedChars.contains(c)) {
                this.guessedChars.add(c);
                result.addAll(this.charPositions.get(c));
            }
        }

        return result;
    }

    /**
     * Check if all letters in the phrase have been completely guessed.
     */
    private boolean isPhraseGuessed() {
        for (Character c: this.phrase) {
            if (!Character.isSpaceChar(c) && !this.guessedChars.contains(c)) {
                return false;
            }
        }

        return true;
    }
}
