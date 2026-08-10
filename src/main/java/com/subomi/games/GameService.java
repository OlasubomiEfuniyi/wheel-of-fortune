package com.subomi.games;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

public class GameService {
    private static GameStorage gameStorage = new GameStorage();

    public static Game createGame(int rounds) {
        if (rounds < 1 || rounds > Game.MAX_NUMBER_OF_ROUNDS) {
            throw new IllegalArgumentException();
        }

        Game game = new Game(new GameBoard(), rounds);

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

    public static List<Player> addPlayersToGame(UUID gameId, List<Player> players) {
        if (gameId == null || players == null || players.size() == 0) {
            throw new IllegalArgumentException();
        }

        Game game = getGameById(gameId);

        List<Player> addedPlayers = game.addPlayers(players);

        return addedPlayers;
    }

    public static boolean removePlayerFromGame(UUID gameId, String playerName) {
        if (gameId == null || playerName == null || playerName.length() == 0) {
            throw new IllegalArgumentException();
        }
        return changeGameState(gameId, (game) -> game.removePlayer(playerName) != null) != null;
    }

    public static Player[] getLeaderboard(UUID gameId) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        Game game = getGameById(gameId);

        return game.getLeaderboard();
    }

    public static GuessResult recordPlayerGuess(Guess guess) {
        if (guess == null) {
            throw new IllegalArgumentException();
        }

        Game game = getGameById(guess.gameId);

        return game.considerPlayerGuess(guess.playerName, guess.guess);
    }
    
    private static Game changeGameState(UUID gameId, Function<Game, Boolean> stateChanger) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        Game game = getGameById(gameId);

        if (stateChanger.apply(game)) {
            gameStorage.saveGame(game);
            return game;
        } 
        else {
            return null;
        }
    }

    private static Game getGameById(UUID gameId) {
        Game game = gameStorage.getGame(gameId);

        if (game == null) {
            throw new NoSuchElementException("Game not found");
        }

        return game;
    }
}
