package com.subomi.games;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameBoard;

public class GameStorageTest {

    @Test
    public void testSaveGame_Saves_New_Game() {
        IGame game = Mockito.mock(IGame.class);
        GameStorage storage = new GameStorage();

        Mockito
        .when(game.getGameId())
        .thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        storage.saveGame(game);
        Assertions.assertNotNull(game.getGameId());
        Assertions.assertNotNull(storage.getGame(game.getGameId()));
    }

    @Test
    public void testSaveGame_Saves_Multiple_Games() {
        IGame game1 = Mockito.mock(IGame.class);
        IGame game2 = Mockito.mock(IGame.class);

        GameStorage storage = new GameStorage();

         Mockito
            .when(game1.getGameId())
            .thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        Mockito
            .when(game2.getGameId())
            .thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        storage.saveGame(game1);
        storage.saveGame(game2);

        Assertions.assertNotNull(game1.getGameId());
        Assertions.assertNotNull(game2.getGameId());

        IGame retrievedGame1 = storage.getGame(game1.getGameId());
        IGame retrievedGame2 = storage.getGame(game2.getGameId());

        Assertions.assertNotNull(retrievedGame1);
        Assertions.assertNotNull(retrievedGame2);

        Assertions.assertEquals(game1.getGameId(), retrievedGame1.getGameId());
        Assertions.assertEquals(game2.getGameId(), retrievedGame2.getGameId());
    }
}
