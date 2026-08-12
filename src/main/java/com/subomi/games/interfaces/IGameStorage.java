package com.subomi.games.interfaces;

import java.util.UUID;

public interface IGameStorage {
    public void saveGame(IGame game);

    public IGame getGame(UUID gameId);
}
