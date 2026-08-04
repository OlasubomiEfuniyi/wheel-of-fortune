package com.subomi.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testAddPlayers_Adds_Players() {
        Game game = new Game(2);
        List<Player> players = Arrays.asList(new Player("subomi"), new Player("nifemi"));
        List<Player> otherPlayers = Arrays.asList(new Player("ayo"), new Player("mide"));

        List<Player> addedPlayers1 = game.addPlayers(players);
        Assertions.assertEquals(2, addedPlayers1.size());
        Assertions.assertEquals(2, game.getPlayers().size());

        Assertions.assertEquals(0, game.addPlayers(players).size());
        Assertions.assertEquals(2, game.addPlayers(otherPlayers).size());

        Assertions.assertEquals(4, game.getPlayers().size());
    }

    @Test
    public void testAddPlayers_Invalid_Player() {
        List<Player> players1 = Arrays.asList(new Player("subomi"), new Player("1player"));
        List<Player> players2 = Arrays.asList(new Player("subomi"), new Player(""));
        List<Player> players3 = Arrays.asList(new Player("subomi"), new Player("playerplayer"));

        Game game = new Game(2);

        Assertions.assertNull(game.addPlayers(players1));
        Assertions.assertNull(game.addPlayers(players2));
        Assertions.assertNull(game.addPlayers(players3));

        Assertions.assertEquals(0, game.getPlayers().size());
    }

    @Test
    public void testAddPlayers_Game_Started() {
        List<Player> players = Arrays.asList(new Player("subomi"));

        Game game = new Game(2);
        
        Assertions.assertTrue(game.start());
        Assertions.assertNull(game.addPlayers(players));
    }

    @Test
    public void testRemovePlayer_Removes_Player() {
        Game game = new Game(2);

        Assertions.assertNotNull(game.addPlayers(Arrays.asList(new Player("subomi"))));
        Player removedPlayer = game.removePlayer("subomi");

        Assertions.assertNotNull(removedPlayer);
        Assertions.assertEquals(0, game.getPlayers().size());
    }

    @Test
    public void testRemovePlayer_Player_Not_Found() {
        Game game = new Game(2);

        Assertions.assertNull(game.removePlayer("subomi"));
    }

    @Test 
    public void testRemovePlayer_Null_Name() {
        Game game = new Game(2);

        Assertions.assertNull(game.removePlayer(null));
    }

    @Test
    public void testNextRound_Advances_Game() {
        Game game = new Game(3);

        Assertions.assertTrue(game.start());

        String phrase1 = game.getPhrase();
        Assertions.assertEquals(1, game.getRound());
        Assertions.assertEquals(GameState.STARTED, game.getGameState());

        Assertions.assertTrue(game.nextRound());
        String phrase2 = game.getPhrase();
        Assertions.assertEquals(2, game.getRound());
        Assertions.assertEquals(GameState.STARTED, game.getGameState());

        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.resume());

        Assertions.assertTrue(game.nextRound());
        String phrase3 = game.getPhrase();
        Assertions.assertEquals(3, game.getRound());
        Assertions.assertEquals(GameState.RESUMED, game.getGameState());

        Assertions.assertTrue(game.nextRound());
        String phrase4 = game.getPhrase();
        Assertions.assertEquals(3, game.getRound());
        Assertions.assertEquals(GameState.ENDED, game.getGameState());

        Assertions.assertFalse(game.nextRound());
        String phrase5 = game.getPhrase();
        Assertions.assertEquals(3, game.getRound());
        Assertions.assertEquals(GameState.ENDED, game.getGameState());

        Assertions.assertNotEquals(phrase1, phrase2);
        Assertions.assertNotEquals(phrase2, phrase3);
        Assertions.assertNotEquals(phrase1, phrase3);
        Assertions.assertNull(phrase4);
        Assertions.assertNull(phrase5);
    }

    @Test
    public void testNextRound_Game_Not_Ongoing() {
        Game game = new Game(3);

        Assertions.assertFalse(game.nextRound());
        Assertions.assertEquals(-1, game.getRound());
    }
}