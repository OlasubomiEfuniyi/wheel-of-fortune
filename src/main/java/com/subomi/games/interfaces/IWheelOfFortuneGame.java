package com.subomi.games.interfaces;

import com.subomi.games.GuessResult;

public interface IWheelOfFortuneGame extends IGame {
    /**
     * Evaluate a players guess
     * @param playerName The name of the player
     * @param guess The player's guess
     * @return GuessResult object representing the result of the evaluation.
     */
    public GuessResult considerPlayerGuess(String playerName, String guess);

    /**
     * Get the phrase on the gameboard.
     */
    public String getPhrase();
}
