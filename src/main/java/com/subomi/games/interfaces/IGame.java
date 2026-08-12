package com.subomi.games.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.subomi.games.GameState;
import com.subomi.games.Player;

public interface IGame {

    /**
     * Get the Id of a game
     */
    public UUID getGameId();

    /**
     * Get the state the game is in
     */

    public GameState getGameState();

    /**
     * Start the game.
     * @return true if the game started successfully, false otherwise.
     */
    public boolean start();

    /**
     * Resume a game.
     * @return true if the game resumed successfully, false otherwise.
     */
    public boolean resume();

    /**
     * End a game.
     * @return true if the game is ended successfully, false otherwise.
     */
    public boolean end();

    /**
     * Pause a game
     * @return true if the game is paused successfully, false otherwise.
     */
    public boolean pause();

    /**
     * Add players to a game.
     * @param players List of players to add.
     * @return List of players added.
     */
    public List<Player> addPlayers(List<Player> players);


    /**
     * Get the players in the game
     */
    public Collection<Player> getPlayers();

    /**
     * Remove a player from the game.
     * @param playerName The name of the player to remove
     * @return The removed player if successful, or null otherwise.
     */
    public Player removePlayer(String playerName);

    /**
     * Advance the game to the next round
     * @return true if the game is successfully advanced, false otherwise
     */
    public boolean nextRound();

    /**
     * Get the current round of the game.
     */
    public int getRound();

    /**
     * Get the number of rounds in the game.
     * @return
     */
    public int getNumRounds();

    /**
     * Get the ordering of players based on points
     */
    public Player[] getLeaderboard();

    /**
     * Determine if a game is actively being played.
     */
    public boolean isGameOngoing();

    /**
     * Determine if a game has been started.
     */
    public boolean isGameStarted();

}
