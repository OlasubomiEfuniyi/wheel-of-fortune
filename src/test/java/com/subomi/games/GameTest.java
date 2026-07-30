package com.subomi.games;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {
    @Test
    public void testGetGameId_Returns_GameId() {
        Game game = new Game(1);

        Assertions.assertNotNull(game.getGameId());
    }

    @Test
    public void testGameConstructor_Invalid_Number_Of_Rounds_1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(0));
    }

    @Test
    public void testGameConstructor_Invalid_Number_Of_Rounds_2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(11));
    }

    @Test
    public void testStart_Starts_Game() {
        Game game = new Game(2);

        Assertions.assertEquals(GameState.CREATED, game.getGameState());

        boolean result = game.start();

        Assertions.assertTrue(result);
        Assertions.assertEquals(GameState.STARTED, game.getGameState());
        Assertions.assertNotNull(game.getPhrase());
        Assertions.assertEquals(1, game.getRound());
    }

    @Test
    public void testStart_After_Start() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertFalse(game.start());

        Assertions.assertEquals(GameState.STARTED, game.getGameState());
    }

    @Test
    public void testPause_Pauses_Game() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());

        boolean result = game.pause();
        
        Assertions.assertTrue(result);
        Assertions.assertEquals(GameState.PAUSED, game.getGameState());
    }

    @Test
    public void testPause_Before_Start() {
        Game game = new Game(2);

        Assertions.assertFalse(game.pause());
        Assertions.assertEquals(GameState.CREATED, game.getGameState());
    }

    @Test
    public void testPause_After_Pause() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());

        Assertions.assertTrue(game.pause());
        Assertions.assertFalse(game.pause());

        Assertions.assertEquals(GameState.PAUSED, game.getGameState());
    }

    @Test
    public void testResume_Resumes_Game() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        String phrase = game.getPhrase();
        int round = game.getRound();

        Assertions.assertTrue(game.pause());
        Assertions.assertEquals(phrase, game.getPhrase());
        Assertions.assertEquals(round, game.getRound());

        Assertions.assertTrue(game.resume());
        Assertions.assertEquals(phrase, game.getPhrase());
        Assertions.assertEquals(round, game.getRound());

        Assertions.assertEquals(GameState.RESUMED, game.getGameState());
    }

    @Test
    public void testResume_Before_Start() {
        Game game = new Game(2);

        Assertions.assertFalse(game.resume());
        Assertions.assertEquals(GameState.CREATED, game.getGameState());
    }

    @Test
    public void testResume_Before_Pause() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());

        Assertions.assertFalse(game.resume());
        Assertions.assertEquals(GameState.STARTED, game.getGameState());
    }

    @Test
    public void testResume_After_Resume() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.resume());

        Assertions.assertFalse(game.resume());
        Assertions.assertEquals(GameState.RESUMED, game.getGameState());
    }

    @Test
    public void testEnd_Ends_Game() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testEnd_Before_Start() {
        Game game = new Game(2);
        
        Assertions.assertFalse(game.end());

        Assertions.assertEquals(GameState.CREATED, game.getGameState());
    }

    @Test
    public void testEnd_During_Pause() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testEnd_After_Resume() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.resume());
        Assertions.assertTrue(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testEnd_After_End() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testStart_After_End() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.start());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testResume_After_End() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.resume());
        
        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testPause_After_End() {
        Game game = new Game(2);

        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.pause());
        
        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }
}