package com.subomi.games;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setup() {
        player = new Player("Subomi");
    }

    @Test
    public void testHandleGuessResult() {
        // Player guessed invalid guess
        player.handleGuessResult(GuessResult.invalidGuess("2", true, null));
        Assertions.assertEquals(0, player.getCash());

        // Player guessed a correct vowel
        player.handleGuessResult(GuessResult.validCorrectOneCharGuess("a", Arrays.asList(0)));
        Assertions.assertEquals(GameConstants.VOWEL_REWARD, player.getCash());

        // Player guessed a correct vowel in multiple positions
        player.handleGuessResult(GuessResult.validCorrectOneCharGuess("a", Arrays.asList(0, 1, 2)));
        Assertions.assertEquals(GameConstants.VOWEL_REWARD + (GameConstants.VOWEL_REWARD * 3), player.getCash());

        // Player guessed a correct consonant
        player.handleGuessResult(GuessResult.validCorrectOneCharGuess("b", Arrays.asList(1)));
        Assertions.assertEquals(
            GameConstants.CONSONANT_REWARD + 
                GameConstants.VOWEL_REWARD + 
                (GameConstants.VOWEL_REWARD * 3), 
            player.getCash());

        // Player guessed a correct consonant in multiple positions
        player.handleGuessResult(GuessResult.validCorrectOneCharGuess("b", Arrays.asList(4, 5, 6)));
        Assertions.assertEquals(
            GameConstants.CONSONANT_REWARD + 
                (GameConstants.CONSONANT_REWARD * 3) +
                GameConstants.VOWEL_REWARD + 
                (GameConstants.VOWEL_REWARD * 3), 
            player.getCash());

        // Player guessed an incorrect vowel
        player.handleGuessResult(GuessResult.validIncorrectOneCharGuess("i", null));
        Assertions.assertEquals(
            GameConstants.CONSONANT_REWARD + 
                (GameConstants.CONSONANT_REWARD * 3) +
                GameConstants.VOWEL_REWARD + 
                (GameConstants.VOWEL_REWARD * 3)+
                GameConstants.VOWEL_PRICE, 
            player.getCash());

        // Player guessed an incorrect consonant
        player.handleGuessResult(GuessResult.validIncorrectOneCharGuess("j", null));
        Assertions.assertEquals(
            GameConstants.CONSONANT_REWARD + 
                (GameConstants.CONSONANT_REWARD * 3) +
                GameConstants.VOWEL_REWARD + 
                (GameConstants.VOWEL_REWARD * 3)+
                GameConstants.VOWEL_PRICE + 
                GameConstants.CONSONANT_PRICE, 
            player.getCash());


        // Player guessed the whole phrase
        player.handleGuessResult(GuessResult.validCorrectWholePhraseGuess("abefgh", Arrays.asList(3, 4, 5)));
    
        Assertions.assertEquals(
            GameConstants.CONSONANT_REWARD + 
                (GameConstants.CONSONANT_REWARD * 3) +
                GameConstants.VOWEL_REWARD + 
                (GameConstants.VOWEL_REWARD * 3)+
                GameConstants.VOWEL_PRICE + 
                GameConstants.CONSONANT_PRICE +
                (GameConstants.WHOLE_PHRASE_PER_LETTER_REWARD * 3) + 
                GameConstants.WHOLE_PHRASE_BONUS, 
            player.getCash());
    }

    @Test
    public void testHandleGuessResult_Null_Guess_Result() {
        Assertions.assertDoesNotThrow(() -> player.handleGuessResult(null));
        Assertions.assertEquals(0, player.getCash());
    }

    @Test
    public void testIsValidPlayer() {
        Player validPlayer = new Player("Subomi");
        Player invalid1 = new Player("1player"); // Contains non alphabetic char
        Player invalid2 = new Player(""); // Empty
        Player invalid3 = new Player("playerplayer"); // Too long
        
        Assertions.assertTrue(Player.isValidPlayer(validPlayer));
        Assertions.assertFalse(Player.isValidPlayer(invalid1));
        Assertions.assertFalse(Player.isValidPlayer(invalid2));
        Assertions.assertFalse(Player.isValidPlayer(invalid3));
    }
}
