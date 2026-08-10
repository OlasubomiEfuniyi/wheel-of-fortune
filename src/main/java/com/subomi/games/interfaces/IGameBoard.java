package com.subomi.games.interfaces;

import com.subomi.games.GuessResult;

public interface IGameBoard {
    /**
     * Get the phrase on the game board.
     */
    public String getPhrase();

    /**
     * Generate the phrase on the game board.
     * @param category The category the phrase should be generated from
     */
    public void generatePhrase(String category);

    /**
     * Evaluate the correctness of a guess
     * @return Object representing the result of the guess.
     */
    public GuessResult considerGuess(String guess);

    /**
     * Clear the phrase on the game board.
     */
    public void clear();
}
