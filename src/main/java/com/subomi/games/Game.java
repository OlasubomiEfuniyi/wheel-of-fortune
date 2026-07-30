package com.subomi.games;

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
        this.round = 1;
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
            this.gameBoard.generatePhrase();
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

    public boolean addPlayers(List<Player> players) {
        if (this.gameState == GameState.CREATED) {
            for(Player player: players) {
                this.players.put(player.playerName(), player);
            }
            return true;
        }
        else {
            return false;
        }
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public Player removePlayer(String playerName) {
        return this.players.remove(playerName);
    }

    public boolean nextRound() {
        if (this.round + 1 <= this.numberOfRounds) {
            this.round++;
            this.gameBoard.generatePhrase();
            return true;
        }
        else {
            return end();
        }
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