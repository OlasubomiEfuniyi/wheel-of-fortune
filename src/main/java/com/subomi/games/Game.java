package com.subomi.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subomi.games.interfaces.IGameBoard;

public class Game {
    public static final int MAX_NUMBER_OF_ROUNDS = 10;
    private UUID gameId;
    private int round;
    private int numberOfRounds;
    private GameState gameState;

    @JsonIgnore 
    private HashMap<String, Player> players;

    @JsonIgnore 
    private IGameBoard gameBoard;

    private Player[] leaderboard;

    public Game(IGameBoard gameBoard, int numberOfRounds) {
        if (numberOfRounds < 1 || numberOfRounds > MAX_NUMBER_OF_ROUNDS) {
            throw new IllegalArgumentException();
        }
        this.gameId = UUID.randomUUID();
        this.round = -1;
        this.numberOfRounds = numberOfRounds;
        this.gameState = GameState.CREATED;
        this.players = new HashMap<>();
        this.gameBoard = gameBoard;
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

            // Initialize the leaderboard with the same player objects
            this.leaderboard = new Player[this.players.size()];
            int index = 0;
            for (Player player: this.players.values()) {
                leaderboard[index++] = player;
            }

            // Order leaderboard by ascending order of player name initially
            Arrays.sort(this.leaderboard, (Player p1, Player p2) -> p1.getPlayerName().compareTo(p2.getPlayerName()));
            
            // Generate the first phrase
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
        if (playerName == null || !this.players.containsKey(playerName)) {
            return null;
        }

        if (guess == null) {
            return null;
        }

        GuessResult guessResult = this.gameBoard.considerGuess(guess);
        Player player = this.players.get(playerName);

        // Update the player based on the guess result
        player.handleGuessResult(guessResult);

        // Update the leaderboard after the player update
        updateLeaderboard();

        return guessResult;
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

    public Player[] getLeaderboard() {
        return this.leaderboard;
    }

    private boolean isGameOngoing() {
        return this.gameState == GameState.STARTED || this.gameState == GameState.RESUMED;
    }

    private boolean isGameStarted() {
        return this.gameState == GameState.STARTED || this.gameState == GameState.RESUMED || this.gameState == GameState.PAUSED;
    }

    /**
     * Order leaderboard in descending order of player cash
     */
    private void updateLeaderboard() {
        Arrays.sort(this.leaderboard, (Player p1, Player p2) -> {
            int result = Double.compare(p1.getCash(), p2.getCash());

            if (result == 0) {
                return p1.getPlayerName().compareTo(p2.getPlayerName());
            }

            return -1 * result;
        });
    }

}