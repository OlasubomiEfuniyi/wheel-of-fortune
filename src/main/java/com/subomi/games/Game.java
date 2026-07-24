package com.subomi.games;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Game {
    private UUID gameId;
    private int playerId;
    private int round;
    private int numberOfRounds;

    @JsonIgnore 
    private HashMap<Integer, Player> players;

    @JsonIgnore 
    private GameBoard gameBoard;

    private GameState gameState;

    public Game(int numberOfRounds) {
        this.playerId = 0;
        this.round = 1;
        this.numberOfRounds = numberOfRounds;
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
            return true;
        }
        else {
            return false;
        }
    }

    public boolean resume() {
        if (this.gameState == GameState.PAUSED) {
            this.gameState = GameState.STARTED;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean end() {
        if (isGameOngoing()) {
            this.gameState = GameState.ENDED;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean addPlayers(List<Player> players) {
        if (this.gameState == GameState.CREATED) {
            for(Player player: players) {
                this.players.put(playerId++, player);
            }
            return true;
        }
        else {
            return false;
        }
    }

    public Player removePlayer(Integer playerId) {
        return this.players.remove(playerId);
    }

    public void nextRound() {
        if (this.round + 1 < this.numberOfRounds) {
            this.round++;
            this.gameBoard.generatePhrase();
        }
        else {
            end();
        }
    }

    private boolean isGameOngoing() {
        return this.gameState == GameState.STARTED || this.gameState == GameState.RESUMED;
    }

}