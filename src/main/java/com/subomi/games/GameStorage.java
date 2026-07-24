package com.subomi.games;

import java.util.HashMap;
import java.util.UUID;

public class GameStorage {
    private HashMap<UUID, Game> games = new HashMap<>();

    public void saveGame(Game game) {
        games.put(game.getGameId(), game);
    }

    public Game getGame(UUID gameId) {
        return games.get(gameId);
    }

}
