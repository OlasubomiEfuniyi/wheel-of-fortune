package com.subomi.games;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameService;
import com.subomi.games.interfaces.IGameStorage;

public class GameService implements IGameService {

    private IGameStorage gameStorage;

    public GameService(IGameStorage gameStorage) {
        this.gameStorage = gameStorage;
    } 

    public IGame createGame(int rounds, GameType gameType) {
        int maxRounds = switch(gameType) {
            case WHEEL_OF_FORTUNE -> WheelOfFortuneGame.MAX_NUMBER_OF_ROUNDS;
            default -> 0;
        };

        if (rounds < 1 || rounds > maxRounds) {
            throw new IllegalArgumentException();
        }

        IGame game = switch(gameType) {
            case WHEEL_OF_FORTUNE -> new WheelOfFortuneGame(new GameBoard(), rounds);
            default -> null;
        };

        this.gameStorage.saveGame(game);
        
        return game;
    }

    public IGame startGame(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.start());
    }

    public IGame resumeGame(UUID gameId) {
       return changeGameState(gameId, (IGame game) -> game.resume());
    }

    public IGame endGame(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.end());
    }

    public IGame pauseGame(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.pause());
    }

    public IGame nextGameRound(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.nextRound());
    }

    public IGame getGame(UUID gameId) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        return this.gameStorage.getGame(gameId);
    }

    public List<Player> addPlayersToGame(UUID gameId, List<Player> players) {
        if (gameId == null || players == null || players.size() == 0) {
            throw new IllegalArgumentException();
        }

        IGame game = getGameById(gameId);

        List<Player> addedPlayers = game.addPlayers(players);

        return addedPlayers;
    }

    public boolean removePlayerFromGame(UUID gameId, String playerName) {
        if (gameId == null || playerName == null || playerName.length() == 0) {
            throw new IllegalArgumentException();
        }
        return changeGameState(gameId, (game) -> game.removePlayer(playerName) != null) != null;
    }

    public Player[] getLeaderboard(UUID gameId) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        IGame game = getGameById(gameId);

        return game.getLeaderboard();
    }
    
    private IGame changeGameState(UUID gameId, Function<IGame, Boolean> stateChanger) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        IGame game = getGameById(gameId);

        if (stateChanger.apply(game)) {
            this.gameStorage.saveGame(game);
            return game;
        } 
        else {
            return null;
        }
    }

    protected IGame getGameById(UUID gameId) {
        IGame game = this.gameStorage.getGame(gameId);

        if (game == null) {
            throw new NoSuchElementException("Game not found");
        }

        return game;
    }
}
