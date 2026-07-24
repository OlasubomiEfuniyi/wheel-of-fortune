package com.subomi.games;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

public class GameService {
    private static GameStorage gameStorage = new GameStorage();

    public static Game createGame() {
        Game game = new Game();

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
