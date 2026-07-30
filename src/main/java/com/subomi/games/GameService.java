package com.subomi.games;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

import com.subomi.games.exceptions.InvalidPlayerExcpetion;

public class GameService {
    private static GameStorage gameStorage = new GameStorage();

    public static Game createGame(int rounds) {
        if (rounds < 1 || rounds > Game.MAX_NUMBER_OF_ROUNDS) {
            throw new IllegalArgumentException();
        }

        Game game = new Game(rounds);

        gameStorage.saveGame(game);
        
        return game;
    }

    public static Game startGame(UUID gameId) {
        return changeGameState(gameId, (Game game) -> game.start());
    }

    public static Game resumeGame(UUID gameId) {
       return changeGameState(gameId, (Game game) -> game.resume());
    }

    public static Game endGame(UUID gameId) {
        return changeGameState(gameId, (Game game) -> game.end());
    }

    public static Game pauseGame(UUID gameId) {
        return changeGameState(gameId, (Game game) -> game.pause());
    }

    public static Game nextGameRound(UUID gameId) {
        return changeGameState(gameId, (Game game) -> game.nextRound());
    }

    public static Game getGame(UUID gameId) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        return gameStorage.getGame(gameId);
    }

    public static Game addPlayersToGame(UUID gameId, List<Player> players) {
        if (gameId == null || players == null || players.size() == 0) {
            throw new IllegalArgumentException();
        }

        boolean invalidPlayerExists = players.stream().anyMatch((player) -> !Player.isValidPlayer(player));

        if (invalidPlayerExists) {
            throw new InvalidPlayerExcpetion();
        }

        return changeGameState(gameId, (Game game) -> game.addPlayers(players));
    }

    public static boolean removePlayerFromGame(UUID gameId, String playerName) {
        if (gameId == null || playerName == null || playerName.length() == 0) {
            throw new IllegalArgumentException();
        }
        return changeGameState(gameId, (game) -> game.removePlayer(playerName) != null) != null;
    }
    
    private static Game changeGameState(UUID gameId, Function<Game, Boolean> stateChanger) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        Game game = gameStorage.getGame(gameId);

        if (game == null) {
            throw new NoSuchElementException("Game not found");
        }
        else if (stateChanger.apply(game)) {
            gameStorage.saveGame(game);
            return game;
        } 
        else {
            return null;
        }
    }
}
