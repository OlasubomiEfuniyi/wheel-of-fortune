package com.subomi.games;

import java.util.HashMap;
import java.util.UUID;

import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameStorage;

public class GameStorage implements IGameStorage{
    private static HashMap<UUID, IGame> games = new HashMap<>();

    public void saveGame(IGame game) {
        games.put(game.getGameId(), game);
    }

    public IGame getGame(UUID gameId) {
        return games.get(gameId);
    }

}
