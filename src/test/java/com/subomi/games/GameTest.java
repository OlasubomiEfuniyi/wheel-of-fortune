package com.subomi.games;

import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.subomi.games.interfaces.IGameBoard;

public class GameTest {
    private Game game;
    private IGameBoard gameBoard;

    @BeforeEach
    public void setup() {
        gameBoard = Mockito.mock(IGameBoard.class);
        game = new Game(gameBoard, 2);
    }

    @Test
    public void testGetGameId_Returns_GameId() {
        Assertions.assertNotNull(game.getGameId());
    }

    @Test
    public void testGameConstructor_Invalid_Number_Of_Rounds_1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(gameBoard, 0));
    }

    @Test
    public void testGameConstructor_Invalid_Number_Of_Rounds_2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(gameBoard, 11));
    }

    @Test
    public void testStart_Starts_Game() {
        Mockito.when(gameBoard.getPhrase()).thenReturn("");
        Assertions.assertEquals(GameState.CREATED, game.getGameState());

        boolean result = game.start();

        Assertions.assertTrue(result);
        Assertions.assertEquals(GameState.STARTED, game.getGameState());
        Assertions.assertNotNull(game.getPhrase());
        Assertions.assertEquals(1, game.getRound());
    }

    @Test
    public void testStart_After_Start() {
        Assertions.assertTrue(game.start());
        Assertions.assertFalse(game.start());

        Assertions.assertEquals(GameState.STARTED, game.getGameState());
    }

    @Test
    public void testPause_Pauses_Game() {
        Assertions.assertTrue(game.start());

        boolean result = game.pause();
        
        Assertions.assertTrue(result);
        Assertions.assertEquals(GameState.PAUSED, game.getGameState());
    }

    @Test
    public void testPause_Before_Start() {
        Assertions.assertFalse(game.pause());
        Assertions.assertEquals(GameState.CREATED, game.getGameState());
    }

    @Test
    public void testPause_After_Pause() {
        Assertions.assertTrue(game.start());

        Assertions.assertTrue(game.pause());
        Assertions.assertFalse(game.pause());

        Assertions.assertEquals(GameState.PAUSED, game.getGameState());
    }

    @Test
    public void testResume_Resumes_Game() {
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
        Assertions.assertFalse(game.resume());
        Assertions.assertEquals(GameState.CREATED, game.getGameState());
    }

    @Test
    public void testResume_Before_Pause() {
        Assertions.assertTrue(game.start());

        Assertions.assertFalse(game.resume());
        Assertions.assertEquals(GameState.STARTED, game.getGameState());
    }

    @Test
    public void testResume_After_Resume() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.resume());

        Assertions.assertFalse(game.resume());
        Assertions.assertEquals(GameState.RESUMED, game.getGameState());
    }

    @Test
    public void testEnd_Ends_Game() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testEnd_Before_Start() {        
        Assertions.assertFalse(game.end());

        Assertions.assertEquals(GameState.CREATED, game.getGameState());
    }

    @Test
    public void testEnd_During_Pause() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testEnd_After_Resume() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.resume());
        Assertions.assertTrue(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testEnd_After_End() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.end());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testStart_After_End() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.start());

        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testResume_After_End() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.resume());
        
        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testPause_After_End() {
        Assertions.assertTrue(game.start());
        Assertions.assertTrue(game.end());
        Assertions.assertFalse(game.pause());
        
        Assertions.assertEquals(GameState.ENDED, game.getGameState());
    }

    @Test
    public void testAddPlayers_Adds_Players() {
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

        Assertions.assertNull(game.addPlayers(players1));
        Assertions.assertNull(game.addPlayers(players2));
        Assertions.assertNull(game.addPlayers(players3));

        Assertions.assertEquals(0, game.getPlayers().size());
    }

    @Test
    public void testAddPlayers_Game_Started() {
        List<Player> players = Arrays.asList(new Player("subomi"));
        
        Assertions.assertTrue(game.start());
        Assertions.assertNull(game.addPlayers(players));
    }

    @Test
    public void testRemovePlayer_Removes_Player() {
        Assertions.assertNotNull(game.addPlayers(Arrays.asList(new Player("subomi"))));
        Player removedPlayer = game.removePlayer("subomi");

        Assertions.assertNotNull(removedPlayer);
        Assertions.assertEquals(0, game.getPlayers().size());
    }

    @Test
    public void testRemovePlayer_Player_Not_Found() {
        Assertions.assertNull(game.removePlayer("subomi"));
    }

    @Test 
    public void testRemovePlayer_Null_Name() {
        Assertions.assertNull(game.removePlayer(null));
    }

    @Test
    public void testNextRound_Advances_Game() {
        Game game = new Game(gameBoard, 3);

        Assertions.assertTrue(game.start());

        Mockito.when(gameBoard.getPhrase()).thenReturn("Phrase1");
        String phrase1 = game.getPhrase();
        Assertions.assertEquals(1, game.getRound());
        Assertions.assertEquals(GameState.STARTED, game.getGameState());

        Assertions.assertTrue(game.nextRound());
        Mockito.when(gameBoard.getPhrase()).thenReturn("Phrase2");
        String phrase2 = game.getPhrase();
        Assertions.assertEquals(2, game.getRound());
        Assertions.assertEquals(GameState.STARTED, game.getGameState());

        Assertions.assertTrue(game.pause());
        Assertions.assertTrue(game.resume());

        Assertions.assertTrue(game.nextRound());
        Mockito.when(gameBoard.getPhrase()).thenReturn("Phrase3");
        String phrase3 = game.getPhrase();
        Assertions.assertEquals(3, game.getRound());
        Assertions.assertEquals(GameState.RESUMED, game.getGameState());

        Assertions.assertTrue(game.nextRound());
        Mockito.when(gameBoard.getPhrase()).thenReturn(null);
        String phrase4 = game.getPhrase();
        Assertions.assertEquals(3, game.getRound());
        Assertions.assertEquals(GameState.ENDED, game.getGameState());

        Assertions.assertFalse(game.nextRound());
        Mockito.when(gameBoard.getPhrase()).thenReturn(null);
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
        Assertions.assertFalse(game.nextRound());
        Assertions.assertEquals(-1, game.getRound());
    }

    @Test
    public void testConsiderPlayerGuess() {
        Player playerA = new Player("PlayerA");
        Player playerB = new Player("PlayerB");
        Player playerC = new Player("PlayerC");

        List<Player> players = Arrays.asList(playerB, playerA, playerC);
        
        game.addPlayers(players);
        game.start();

        Player[] leaderboard = game.getLeaderboard();

        Assertions.assertEquals("PlayerA", leaderboard[0].getPlayerName());
        Assertions.assertEquals("PlayerB", leaderboard[1].getPlayerName());
        Assertions.assertEquals("PlayerC", leaderboard[2].getPlayerName());
 
        // Player B guessed a correct vowel
        String guess = "a";
        Mockito
            .when(gameBoard.considerGuess(guess))
            .thenReturn(GuessResult.validCorrectOneCharGuess(guess, Arrays.asList(0)));

        GuessResult guessResult = game.considerPlayerGuess(playerB.getPlayerName(), guess);
        leaderboard = game.getLeaderboard();

        Assertions.assertNotNull(guessResult);
        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertEquals(playerB, leaderboard[0]);
        Assertions.assertEquals(playerA, leaderboard[1]);
        Assertions.assertEquals(playerC, leaderboard[2]);

        Assertions.assertEquals(0, playerA.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, playerB.getCash());
        Assertions.assertEquals(0, playerC.getCash());

        // PlayerA guessed a correct consonant
        guess = "b";
        Mockito
            .when(gameBoard.considerGuess(guess))
            .thenReturn(GuessResult.validCorrectOneCharGuess(guess, Arrays.asList(1)));
        guessResult = game.considerPlayerGuess(playerA.getPlayerName(), guess);
        leaderboard = game.getLeaderboard();

        Assertions.assertNotNull(guessResult);
        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertEquals(playerA, leaderboard[0]);
        Assertions.assertEquals(playerB, leaderboard[1]);
        Assertions.assertEquals(playerC, leaderboard[2]);

        Assertions.assertEquals(GameConstants.CONSONANT_REWARD, playerA.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, playerB.getCash());
        Assertions.assertEquals(0, playerC.getCash());

        // PlayerC guessed a correct vowel
        guess = "e";
        Mockito
            .when(gameBoard.considerGuess(guess))
            .thenReturn(GuessResult.validCorrectOneCharGuess(guess, Arrays.asList(2)));
        guessResult = game.considerPlayerGuess(playerC.getPlayerName(), guess);
        leaderboard = game.getLeaderboard();

        Assertions.assertNotNull(guessResult);
        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertEquals(playerA, leaderboard[0]);
        Assertions.assertEquals(playerB, leaderboard[1]);
        Assertions.assertEquals(playerC, leaderboard[2]);

        Assertions.assertEquals(GameConstants.CONSONANT_REWARD, playerA.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, playerB.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, playerC.getCash());

        // PlayerB guessed an incorrect vowel
        guess = "u";
        Mockito
            .when(gameBoard.considerGuess(guess))
            .thenReturn(GuessResult.validIncorrectOneCharGuess(guess, null));

        guessResult = game.considerPlayerGuess(playerB.getPlayerName(), guess);
        leaderboard = game.getLeaderboard();

        Assertions.assertNotNull(guessResult);
        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertEquals(playerA, leaderboard[0]);
        Assertions.assertEquals(playerC, leaderboard[1]);
        Assertions.assertEquals(playerB, leaderboard[2]);

        Assertions.assertEquals(GameConstants.CONSONANT_REWARD, playerA.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD + GameConstants.VOWEL_PRICE, playerB.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, playerC.getCash());

        // PlayerA guessed an incorrect consonant
        guess = "z";
        Mockito
            .when(gameBoard.considerGuess(guess))
            .thenReturn(GuessResult.validIncorrectOneCharGuess(guess, null));

        guessResult = game.considerPlayerGuess(playerA.getPlayerName(), guess);
        leaderboard = game.getLeaderboard();

        Assertions.assertNotNull(guessResult);
        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertEquals(playerA, leaderboard[0]);
        Assertions.assertEquals(playerC, leaderboard[1]);
        Assertions.assertEquals(playerB, leaderboard[2]);

        Assertions.assertEquals(GameConstants.CONSONANT_REWARD + GameConstants.CONSONANT_PRICE, playerA.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD + GameConstants.VOWEL_PRICE, playerB.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, playerC.getCash());

        // PlayerA - Invalid guess, everything remains the same
        guess = "2";
        Mockito
            .when(gameBoard.considerGuess(guess))
            .thenReturn(GuessResult.invalidGuess(guess, true, null));

        guessResult = game.considerPlayerGuess(playerA.getPlayerName(), guess);
        leaderboard = game.getLeaderboard();

        Assertions.assertNotNull(guessResult);
        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertEquals(playerA, leaderboard[0]);
        Assertions.assertEquals(playerC, leaderboard[1]);
        Assertions.assertEquals(playerB, leaderboard[2]);

        Assertions.assertEquals(GameConstants.CONSONANT_REWARD + GameConstants.CONSONANT_PRICE, playerA.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD + GameConstants.VOWEL_PRICE, playerB.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, playerC.getCash());

        // PlayerC guessed the whole phrase
        guess = "abefgh";
        Mockito
            .when(gameBoard.considerGuess(guess))
            .thenReturn(GuessResult.validCorrectWholePhraseGuess(guess, Arrays.asList(3, 4, 5)));
        guessResult = game.considerPlayerGuess(playerC.getPlayerName(), guess);
        leaderboard = game.getLeaderboard();

        Assertions.assertNotNull(guessResult);
        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertEquals(playerC, leaderboard[0]);
        Assertions.assertEquals(playerA, leaderboard[1]);
        Assertions.assertEquals(playerB, leaderboard[2]);

        Assertions.assertEquals(GameConstants.CONSONANT_REWARD + GameConstants.CONSONANT_PRICE, playerA.getCash());
        Assertions.assertEquals(GameConstants.VOWEL_REWARD + GameConstants.VOWEL_PRICE, playerB.getCash());
        Assertions.assertEquals(
            GameConstants.VOWEL_REWARD + 
                (GameConstants.WHOLE_PHRASE_PER_LETTER_REWARD * 3) + 
                GameConstants.WHOLE_PHRASE_BONUS
            , playerC.getCash());
    }

    @Test
    public void testConsiderPlayerGuess_Null_Player_Name() {
        Assertions.assertNull(game.considerPlayerGuess(null, "a"));
    }

    @Test
    public void testConsiderPlayerGuess_Null_Guess() {
        Player player1 = new Player("Subomi");
        game.addPlayers(Arrays.asList(player1));

        Assertions.assertNull(game.considerPlayerGuess(player1.getPlayerName(), null));
    }

    @Test
    public void testConsiderPlayerGuess_Player_Not_Found() {
        Player player1 = new Player("Subomi");
        game.addPlayers(Arrays.asList(player1));
        
        Assertions.assertNull(game.considerPlayerGuess("Marco", "a"));
    }

}