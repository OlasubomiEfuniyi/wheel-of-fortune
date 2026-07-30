package com.subomi.games;

public record Player(String playerName) {

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
}
