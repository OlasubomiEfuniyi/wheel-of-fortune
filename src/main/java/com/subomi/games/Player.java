package com.subomi.games;

public class  Player {

    private String playerName;
    private int points;
    private double cash;

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Given the result of a guess by this player, update the relevant fields.
     * This method will update the amount of cash the player has based on if 
     * they guessed one character or the whole phrase, and if it is correct.
     * 
     * When a player guessed one character and it is correct, they receive a cash reward
     * depending on if their guess is a vowel or a consonant, and the number of positions
     * they got correct. If the player guessed one character and it is wrong, 
     * they get deducted the cost of that character category (vowel or consonant).
     * 
     * When a player guessed the whole phrase and it is correct, they receive a cash 
     * reward for each position that they filled in, plus a whole phrase bonus.
     */
    public void handleGuessResult(GuessResult guessResult) {
        // Update points based on if guess was correct and if it completed the phrase
        if (guessResult.isOneCharGuess && !guessResult.isGuessCorrect) {
            if (isVowel(guessResult.guess.charAt(0))) {
                this.cash -= GameConstants.VOWEL_PRICE;
            }
            else {
                this.cash -= GameConstants.CONSONANT_PRICE;
            }
        }
        else if (guessResult.isOneCharGuess && guessResult.isGuessCorrect) {
            if (isVowel(guessResult.guess.charAt(0))) {
                this.cash += GameConstants.VOWEL_REWARD * guessResult.guessedPositions.size();
            }
            else {
                this.cash += GameConstants.CONSONANT_REWARD * guessResult.guessedPositions.size();
            }
        }
        else if (guessResult.isGuessCorrect) {
            this.cash += (guessResult.guessedPositions.size() * GameConstants.WHOLE_PHRASE_PER_LETTER_REWARD) + GameConstants.WHOLE_PHRASE_BONUS;
        }
    }

    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    /**
     * Check if a player object is valid
     */
    public static boolean isValidPlayer(Player player) {
        return validPlayerName(player.playerName);
    }
    
    /**
     * This method checks if a player name is valid.
     * A valid player name is at least 1 character and at
     * most 10 characters long, and only consists of letters 
     * in the alphabet.
     * 
     * @return True if a name is valid, and False otherwise.
     */
    public static boolean validPlayerName(String playerName) {
        if (playerName == null) {
            return false;
        }
        if (playerName.length() < 1 || playerName.length() > 10) {
            return false;
        }

        for (char c: playerName.toCharArray()) {
            if (!Character.isAlphabetic(c)) {
                return false;
            }
        }

        return true;
    }

    public double getCash() {
        return this.cash;
    }
}
