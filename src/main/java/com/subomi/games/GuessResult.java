package com.subomi.games;

import java.util.List;

public class GuessResult {
    public String guess;
    public boolean isGuessValid;
    public boolean isGuessCorrect;
    public boolean didGuessCompletePhrase;
    public boolean isOneCharGuess;
    public String reason;
    public List<Integer> guessedPositions;

    private GuessResult(
        String guess,
        boolean isGuessValid,
        boolean isGuessCorrect, 
        boolean didGuessCompletePhrase, 
        boolean isOneCharGuess,
        String reason,
        List<Integer> guessedPositions) {
        this.isGuessValid = isGuessValid;
        this.isGuessCorrect = isGuessCorrect;
        this.didGuessCompletePhrase = didGuessCompletePhrase;
        this.isOneCharGuess = isOneCharGuess;
        this.guessedPositions = guessedPositions;
    }

    public static GuessResult invalidGuess(String guess, boolean isOneCharGuess, String reason) {
        return new GuessResult(
            guess,
            false, 
            false,
            false, 
            isOneCharGuess, 
            reason, 
            null);
    }

    public static GuessResult validCorrectOneCharGuessThatCompletes(String guess, List<Integer> positions) {
        return new GuessResult(
            guess,
            true, 
            true, 
            true, 
            true, 
            null, 
            positions);
    }

    public static GuessResult validCorrectWholePhraseGuess(String guess, List<Integer> positions) {
        return new GuessResult(
            guess,
            true, 
            true, 
            true, 
            false, 
            null, 
            positions);
    }

    public static GuessResult validCorrectOneCharGuess(String guess, List<Integer> positions) {
        return new GuessResult(
            guess,
            true, 
            true, 
            false, 
            true, 
            null,
            positions);
    }

    public static GuessResult validIncorrectOneCharGuess(String guess, String reason) {
        return new GuessResult(
            guess,
            true, 
            false, 
            false, 
            true, 
            reason,
            null);
    }

    public static GuessResult validIncorrectWholePhraseGuess(String guess, String reason) {
        return new GuessResult(
            guess,
            true, 
            false, 
            false, 
            false, 
            reason,
            null);
    }
}
