package com.subomi.games.interfaces;

import java.util.UUID;

public interface IGameStorage<T extends IGame> {
    public void saveGame(T game);

    public T getGame(UUID gameId);
}
