package com.subomi.games;

import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameStorage;
import com.subomi.games.interfaces.IWheelOfFortuneGame;
import com.subomi.games.interfaces.IWheelOfFortuneGameService;

public class WheelOfFortuneGameService extends GameService implements IWheelOfFortuneGameService {
    public WheelOfFortuneGameService(IGameStorage gameStorage) {
        super(gameStorage);
    }

    public GuessResult recordPlayerGuess(Guess guess) {
        if (guess == null) {
            throw new IllegalArgumentException();
        }

        IGame game = getGameById(guess.gameId);

        if (game instanceof IWheelOfFortuneGame) {
            return ((IWheelOfFortuneGame) game).considerPlayerGuess(guess.playerName, guess.guess);
        }
        else {
            throw new RuntimeException("Not a Wheel of Fortune game ID");
        }

    }
}
