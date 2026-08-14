package com.subomi.games;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameStorage;
import com.subomi.games.interfaces.IWheelOfFortuneGame;
import com.subomi.games.interfaces.IWheelOfFortuneGameService;

public class WheelOfFortuneGameService implements IWheelOfFortuneGameService{
    private IGameStorage<IWheelOfFortuneGame> gameStorage;

    public WheelOfFortuneGameService(IGameStorage<IWheelOfFortuneGame> gameStorage) {
        this.gameStorage = gameStorage;
    } 

    public IWheelOfFortuneGame createGame(int numRounds) {
        if (numRounds < 1 || numRounds > WheelOfFortuneGame.MAX_NUMBER_OF_ROUNDS) {
            throw new IllegalArgumentException();
        }

        IWheelOfFortuneGame game = new WheelOfFortuneGame(new GameBoard(), numRounds);

        this.gameStorage.saveGame(game); 
        
        return game;
    }

    public GuessResult recordPlayerGuess(Guess guess) {
        if (guess == null) {
            throw new IllegalArgumentException();
        }

        IWheelOfFortuneGame game = getGameById(guess.gameId);

        if (game instanceof IWheelOfFortuneGame) {
            return ((IWheelOfFortuneGame) game).considerPlayerGuess(guess.playerName, guess.guess);
        }
        else {
            throw new RuntimeException("Not a Wheel of Fortune game ID");
        }

    }

    public IWheelOfFortuneGame startGame(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.start());
    }

    public IWheelOfFortuneGame resumeGame(UUID gameId) {
       return changeGameState(gameId, (IGame game) -> game.resume());
    }

    public IWheelOfFortuneGame endGame(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.end());
    }

    public IWheelOfFortuneGame pauseGame(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.pause());
    }

    public IWheelOfFortuneGame nextGameRound(UUID gameId) {
        return changeGameState(gameId, (IGame game) -> game.nextRound());
    }

    public IWheelOfFortuneGame getGame(UUID gameId) {
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
    
    private IWheelOfFortuneGame changeGameState(UUID gameId, Function<IGame, Boolean> stateChanger) {
        if (gameId == null) {
            throw new IllegalArgumentException();
        }

        IWheelOfFortuneGame game = getGameById(gameId);

        if (stateChanger.apply(game)) {
            this.gameStorage.saveGame(game);
            return game;
        } 
        else {
            return null;
        }
    }

    protected IWheelOfFortuneGame getGameById(UUID gameId) {
        IWheelOfFortuneGame game = this.gameStorage.getGame(gameId);

        if (game == null) {
            throw new NoSuchElementException("Game not found");
        }

        return game;
    }
}
