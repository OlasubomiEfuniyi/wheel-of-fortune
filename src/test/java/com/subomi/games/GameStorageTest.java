package com.subomi.games;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameStorageTest {
    @Test
    public void testSaveGame_Saves_New_Game() {
        Game game = new Game(4);
        GameStorage storage = new GameStorage();

        storage.saveGame(game);
        Assertions.assertNotNull(game.getGameId());
        Assertions.assertNotNull(storage.getGame(game.getGameId()));
    }

    @Test
    public void testSaveGame_Saves_Multiple_Games() {
        Game game1 = new Game(1);
        Game game2 = new Game(3);

        GameStorage storage = new GameStorage();

        storage.saveGame(game1);
        storage.saveGame(game2);

        Assertions.assertNotNull(game1.getGameId());
        Assertions.assertNotNull(game2.getGameId());

        Game retrievedGame1 = storage.getGame(game1.getGameId());
        Game retrievedGame2 = storage.getGame(game2.getGameId());

        Assertions.assertNotNull(retrievedGame1);
        Assertions.assertNotNull(retrievedGame2);

        Assertions.assertEquals(game1.getGameId(), retrievedGame1.getGameId());
        Assertions.assertEquals(game2.getGameId(), retrievedGame2.getGameId());
    }
}
