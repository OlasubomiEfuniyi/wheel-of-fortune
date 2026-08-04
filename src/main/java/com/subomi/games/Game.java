package com.subomi.games;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Game {
    public static final int MAX_NUMBER_OF_ROUNDS = 10;
    private UUID gameId;
    private int round;
    private int numberOfRounds;

    @JsonIgnore 
    private HashMap<String, Player> players;

    @JsonIgnore 
    private GameBoard gameBoard;

    private GameState gameState;

    public Game(int numberOfRounds) {
        if (numberOfRounds < 1 || numberOfRounds > MAX_NUMBER_OF_ROUNDS) {
            throw new IllegalArgumentException();
        }
        this.gameId = UUID.randomUUID();
        this.round = -1;
        this.numberOfRounds = numberOfRounds;
        this.gameState = GameState.CREATED;
        this.players = new HashMap<>();
        this.gameBoard = new GameBoard();
    }

    public UUID getGameId() {
        return this.gameId;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public boolean start() {
        if (this.gameState == GameState.CREATED) {
            this.gameState = GameState.STARTED;
            this.round = 1; 
            this.gameBoard.generatePhrase(null);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean resume() {
        if (this.gameState == GameState.PAUSED) {
            this.gameState = GameState.RESUMED;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean end() {
        if (isGameStarted()) {
            this.gameState = GameState.ENDED;
            this.gameBoard.clear();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pause() {
        if (isGameOngoing()) {
            this.gameState = GameState.PAUSED;
            return true;
        }
        else {
            return false;
        }
    }

    public List<Player> addPlayers(List<Player> players) {
        ArrayList<Player> addedPlayers = new ArrayList<>();

        if (players == null || players.size() == 0) {
            return null;
        }

        boolean containsInvalidPlayer = players.stream().anyMatch((player) -> !Player.isValidPlayer(player));

        if (containsInvalidPlayer) {
            return null;
        }

        if (this.gameState == GameState.CREATED) {
            for(Player player: players) {
                if (this.players.containsKey(player.getPlayerName())) {
                    continue;
                }

                this.players.put(player.getPlayerName(), player);
                addedPlayers.add(player);
            }

            return addedPlayers;
        }
        else {
            return null;
        }
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public Player removePlayer(String playerName) {
        if (playerName == null) {
            return null;
        }

        return this.players.remove(playerName);
    }

    public boolean nextRound() {
        if (!isGameOngoing()) {
            return false;
        }

        if (this.round + 1 <= this.numberOfRounds) {
            this.round++;
            this.gameBoard.generatePhrase(null);
            return true;
        }
        else {
            return end();
        }
    }

    public GuessResult considerPlayerGuess(String playerName, String guess) {
        if (playerName == null) {
            return null;
        }

        if (guess == null) {
            return null;
        }

        GuessResult result = this.gameBoard.considerGuess(guess);

        // Update the player based on the guess result
        this.players.get(playerName).handleGuessResult(result);

        // Update the scoreboard based on the guess result
        
        return result;
    }

    public int getRound() {
        return this.round;
    }

    public int getNumRounds() {
        return this.numberOfRounds;
    }

    public String getPhrase() {
        return this.gameBoard.getPhrase();
    }

    private boolean isGameOngoing() {
        return this.gameState == GameState.STARTED || this.gameState == GameState.RESUMED;
    }

    private boolean isGameStarted() {
        return this.gameState == GameState.STARTED || this.gameState == GameState.RESUMED || this.gameState == GameState.PAUSED;
    }

}