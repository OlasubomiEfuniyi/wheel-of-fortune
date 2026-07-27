package com.subomi.games;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

public class GameService {
    private static GameStorage gameStorage = new GameStorage();

    public static Game createGame(int rounds) {
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

    public static Game addPlayersToGame(UUID gameId, List<Player> players) {
        return changeGameState(gameId, (Game game) -> game.addPlayers(players));
    }
    
    private static Game changeGameState(UUID gameId, Function<Game, Boolean> stateChanger) {
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
