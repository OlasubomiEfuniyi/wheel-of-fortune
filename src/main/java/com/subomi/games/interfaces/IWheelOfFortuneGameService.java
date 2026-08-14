package com.subomi.games.interfaces;

import com.subomi.games.Guess;
import com.subomi.games.GuessResult;

public interface IWheelOfFortuneGameService extends IGameService<IWheelOfFortuneGame> {
    public GuessResult recordPlayerGuess(Guess guess);
}
