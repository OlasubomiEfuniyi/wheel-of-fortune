package com.subomi.games;

import java.util.HashMap;
import java.util.UUID;

import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameStorage;

public class GameStorage<T extends IGame> implements IGameStorage<T> {
    private static HashMap<UUID, Object> games = new HashMap<>();

    public void saveGame(T game) {
        games.put(game.getGameId(), game);
    }

    @SuppressWarnings("unchecked")
    public T getGame(UUID gameId) {
        return (T)games.get(gameId);
    }

}
