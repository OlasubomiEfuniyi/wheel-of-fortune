package com.subomi.games;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameBoardTest {
    private GameBoard gameBoard;

    @BeforeEach
    public void setup() {
        gameBoard = new GameBoard();
    }

    @Test
    public void testGeneratePhrase() {
        Assertions.assertNull(gameBoard.getPhrase());
        gameBoard.generatePhrase(null);
        Assertions.assertNotNull(gameBoard.getPhrase());
    }
    @Test
    public void testConsiderGuess_Correct_One_Letter_Guess_1() {
        gameBoard.setPhrase("Hello Hello Hello");

        String guess = "h";
        GuessResult guessResult = gameBoard.considerGuess(guess);

        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertEquals(3, guessResult.guessedPositions.size());

        Assertions.assertTrue(postionsEquality(Arrays.asList(0, 6, 12), guessResult.guessedPositions));
        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertTrue(guessResult.isOneCharGuess);
        Assertions.assertEquals(guess, guessResult.guess);
    }

    @Test
    public void testConsiderGuess_Correct_One_Letter_Guess_2() {
        gameBoard.setPhrase("Hello Hello Hello");

        String guess = "H";
        GuessResult guessResult = gameBoard.considerGuess(guess);

        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertEquals(3, guessResult.guessedPositions.size());

        Assertions.assertTrue(postionsEquality(Arrays.asList(0, 6, 12), guessResult.guessedPositions));
        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertTrue(guessResult.isOneCharGuess);
        Assertions.assertEquals(guess.toLowerCase(), guessResult.guess);
    }

    @Test
    public void testConsiderGuess_Correct_Whole_Phrase_Guess() {
        gameBoard.setPhrase("Hello Hello Hello");

        String guess = "hello hello hello";
        GuessResult guessResult = gameBoard.considerGuess(guess);

        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertEquals(15, guessResult.guessedPositions.size());

        Assertions.assertTrue(postionsEquality(Arrays.asList(0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16), guessResult.guessedPositions));
        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertTrue(guessResult.didGuessCompletePhrase);
        Assertions.assertFalse(guessResult.isOneCharGuess);
        Assertions.assertEquals(guess, guessResult.guess);
    }

    @Test
    public void testConsiderGuess_Correct_One_Letter_Guess_Then_Whole_Phrase() {
        gameBoard.setPhrase("Hello Hello Hello");

        String guess1 = "h";
        String guess2 = "e";
        String guess3 = "hello hello hello";

        GuessResult guessResult1 = gameBoard.considerGuess(guess1);
        GuessResult guessResult2 = gameBoard.considerGuess(guess2);
        GuessResult guessResult3 = gameBoard.considerGuess(guess3);

        Assertions.assertTrue(guessResult1.isGuessCorrect);
        Assertions.assertEquals(3, guessResult1.guessedPositions.size());
        Assertions.assertTrue(postionsEquality(Arrays.asList(0, 6, 12), guessResult1.guessedPositions));
        Assertions.assertTrue(guessResult1.isGuessValid);
        Assertions.assertFalse(guessResult1.didGuessCompletePhrase);
        Assertions.assertTrue(guessResult1.isOneCharGuess);
        Assertions.assertEquals(guess1, guessResult1.guess);

        Assertions.assertTrue(guessResult2.isGuessCorrect);
        Assertions.assertEquals(3, guessResult2.guessedPositions.size());
        Assertions.assertTrue(postionsEquality(Arrays.asList(1, 7, 13), guessResult2.guessedPositions));
        Assertions.assertTrue(guessResult2.isGuessValid);
        Assertions.assertFalse(guessResult2.didGuessCompletePhrase);
        Assertions.assertTrue(guessResult2.isOneCharGuess);
        Assertions.assertEquals(guess2, guessResult2.guess);

        Assertions.assertTrue(guessResult3.isGuessCorrect);
        Assertions.assertEquals(9, guessResult3.guessedPositions.size());
        Assertions.assertTrue(postionsEquality(Arrays.asList(2, 3, 4, 8, 9, 10, 14, 15, 16), guessResult3.guessedPositions));
        Assertions.assertTrue(guessResult3.isGuessValid);
        Assertions.assertTrue(guessResult3.didGuessCompletePhrase);
        Assertions.assertFalse(guessResult3.isOneCharGuess);
        Assertions.assertEquals(guess3, guessResult3.guess);
    }

    @Test
    public void testConsiderGuess_Incorrect_One_Letter_Guess() {
        gameBoard.setPhrase("Hello Hello Hello");

        String guess = "b";
        GuessResult guessResult = gameBoard.considerGuess(guess);

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertNull(guessResult.guessedPositions);

        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertTrue(guessResult.isOneCharGuess);
        Assertions.assertEquals(guess, guessResult.guess);
    }

    @Test
    public void testConsiderGuess_Incorrect_Whole_Phrase_Guess() {
        gameBoard.setPhrase("Hello Hello Hello");

        String guess = "hello hello hellb";
        GuessResult guessResult = gameBoard.considerGuess(guess);

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertNull(guessResult.guessedPositions);

        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertFalse(guessResult.isOneCharGuess);
        Assertions.assertEquals(guess, guessResult.guess);
    }

    @Test
    public void testConsiderGuess_Correct_Then_Incorrect_One_Letter_Guess() {
        gameBoard.setPhrase("Hello Hello Hello");

        String guess1 = "l";
        String guess2 = "b";

        GuessResult guessResult1 = gameBoard.considerGuess(guess1);
        GuessResult guessResult2 = gameBoard.considerGuess(guess2);

        Assertions.assertTrue(guessResult1.isGuessCorrect);
        Assertions.assertTrue(postionsEquality(Arrays.asList(2, 3 ,8, 9, 14, 15), guessResult1.guessedPositions));
        Assertions.assertTrue(guessResult1.isGuessValid);
        Assertions.assertFalse(guessResult1.didGuessCompletePhrase);
        Assertions.assertTrue(guessResult1.isOneCharGuess);
        Assertions.assertEquals(guess1, guessResult1.guess);

        Assertions.assertFalse(guessResult2.isGuessCorrect);
        Assertions.assertNull(guessResult2.guessedPositions);
        Assertions.assertTrue(guessResult2.isGuessValid);
        Assertions.assertFalse(guessResult2.didGuessCompletePhrase);
        Assertions.assertTrue(guessResult2.isOneCharGuess);
        Assertions.assertEquals(guess2, guessResult2.guess);
    }

    @Test
    public void testConsiderGuess_Multiple_Guesses_1() {
        gameBoard.setPhrase("a b c a b c");

        gameBoard.considerGuess("a");
        gameBoard.considerGuess("c");
        GuessResult guessResult3 = gameBoard.considerGuess("b");

        Assertions.assertTrue(guessResult3.isGuessCorrect);
        Assertions.assertTrue(guessResult3.isOneCharGuess);
        Assertions.assertTrue(guessResult3.isGuessValid);
        Assertions.assertTrue(guessResult3.didGuessCompletePhrase);
        Assertions.assertEquals(2, guessResult3.guessedPositions.size());
        Assertions.assertTrue(postionsEquality(Arrays.asList(2, 8), guessResult3.guessedPositions));
    }

    @Test
    public void testConsiderGuess_Multiple_Guesses_2() {
        gameBoard.setPhrase("a b c a b c");

        gameBoard.considerGuess("a");
        gameBoard.considerGuess("a b c a b d");
        GuessResult guessResult = gameBoard.considerGuess("a b c a b c");

        Assertions.assertTrue(guessResult.isGuessCorrect);
        Assertions.assertFalse(guessResult.isOneCharGuess);
        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertTrue(guessResult.didGuessCompletePhrase);
        Assertions.assertEquals(4, guessResult.guessedPositions.size());
        Assertions.assertTrue(postionsEquality(Arrays.asList(2, 4, 8, 10), guessResult.guessedPositions));
    }

    @Test
    public void testConsiderGuess_Already_Guessed() {
        gameBoard.setPhrase("a b c a b c");

        gameBoard.considerGuess("a");
        GuessResult guessResult = gameBoard.considerGuess("a");

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertTrue(guessResult.isOneCharGuess);
        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertNull(guessResult.guessedPositions);
    }

    @Test
    public void testConsiderGuess_Guess_After_Phrase_Guessed() {
        gameBoard.setPhrase("a b c a b c");

        gameBoard.considerGuess("a b c a b c");
        GuessResult guessResult = gameBoard.considerGuess("a");

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertTrue(guessResult.isOneCharGuess);
        Assertions.assertTrue(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertNull(guessResult.guessedPositions);
    }

    @Test
    public void testConsiderGuess_Invalid_One_Letter_Guess() {
        gameBoard.setPhrase("a b c a b c");

        GuessResult guessResult = gameBoard.considerGuess("@");

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertTrue(guessResult.isOneCharGuess);
        Assertions.assertFalse(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertNull(guessResult.guessedPositions);
    }

    @Test
    public void testConsiderGuess_Invalid_Multiple_Letter_Guess_1() {
        gameBoard.setPhrase("a b c a b c");

        GuessResult guessResult = gameBoard.considerGuess("a b c");

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertFalse(guessResult.isOneCharGuess);
        Assertions.assertFalse(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertNull(guessResult.guessedPositions);
    }

    @Test
    public void testConsiderGuess_Invalid_Multiple_Letter_Guess_2() {
        gameBoard.setPhrase("a b c a b c");

        GuessResult guessResult = gameBoard.considerGuess("a b c a b @");

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertFalse(guessResult.isOneCharGuess);
        Assertions.assertFalse(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertNull(guessResult.guessedPositions);
        Assertions.assertTrue(guessResult.reason.contains("invalid characters"));
    }

    @Test
    public void testConsiderGuess_Invalid_Multiple_Letter_Guess_3() {
        gameBoard.setPhrase("a b c a b c");

        GuessResult guessResult = gameBoard.considerGuess("a b c a b   c");

        Assertions.assertFalse(guessResult.isGuessCorrect);
        Assertions.assertFalse(guessResult.isOneCharGuess);
        Assertions.assertFalse(guessResult.isGuessValid);
        Assertions.assertFalse(guessResult.didGuessCompletePhrase);
        Assertions.assertNull(guessResult.guessedPositions);
        Assertions.assertTrue(guessResult.reason.contains("invalid characters"));
    }

    @Test
    public void testConsiderGuess_Trims_Spaces_1() {
        gameBoard.setPhrase("a b c a b c");

        GuessResult guessResult = gameBoard.considerGuess(" a b c a b c ");

        Assertions.assertTrue(guessResult.isGuessCorrect);
    }

    @Test
    public void testConsiderGuess_Trims_Spaces_2() {
        gameBoard.setPhrase("a b c a b c");

        GuessResult guessResult = gameBoard.considerGuess(" a ");

        Assertions.assertTrue(guessResult.isGuessCorrect);
    }

    @Test
    public void testConsiderGuess_Null_Guess() {
        Assertions.assertNull(gameBoard.considerGuess(null));
    }

    private boolean postionsEquality(List<Integer> expected, List<Integer> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }

        for (Integer i: actual) {
            if (!expected.contains(i)) {
                return false;
            }
        }

        return true;
    }
}
