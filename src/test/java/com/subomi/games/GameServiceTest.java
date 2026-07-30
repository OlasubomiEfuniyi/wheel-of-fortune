package com.subomi.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.subomi.games.exceptions.InvalidPlayerExcpetion;

public class GameServiceTest {

    @Test
    public void testCreateGame_Creates_New_Game() {
        Game game = GameService.createGame(1);

        Assertions.assertNotNull(game);
        Assertions.assertEquals(1, game.getNumRounds());
        Assertions.assertEquals(GameState.CREATED, game.getGameState());
        Assertions.assertEquals(1, game.getRound());
        Assertions.assertEquals(0, game.getPlayers().size());

    
        Game retrievedGame = GameService.getGame(game.getGameId());
        Assertions.assertNotNull(retrievedGame);
        Assertions.assertEquals(game.getGameId(), retrievedGame.getGameId());
    }

    @Test
    public void testCreateGame_Creates_Multiple_Games() {
        Game game1 = GameService.createGame(1);
        Game game2 = GameService.createGame(1);

        Assertions.assertNotNull(game1);
        Assertions.assertNotNull(game2);

        Assertions.assertNotNull(GameService.getGame(game1.getGameId()));
        Assertions.assertNotNull(GameService.getGame(game2.getGameId()));
    }

    @Test
    public void testCreateGame_Invalid_Rounds() {
         Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.createGame(0));
    }

    static Stream<Arguments> stateChangers() {
        return Stream.of(
            Arguments.of((Function<UUID, Game>) (gameId) -> {
                return GameService.startGame(gameId);
            }, GameState.STARTED),
            Arguments.of((Function<UUID, Game>) (gameId) -> {
                GameService.startGame(gameId);
                GameService.pauseGame(gameId);
                return GameService.resumeGame(gameId);
            }, GameState.RESUMED),
            Arguments.of((Function<UUID, Game>) (gameId) -> {
                GameService.startGame(gameId);
                return GameService.endGame(gameId);
            }, GameState.ENDED),
            Arguments.of((Function<UUID, Game>) (gameId) -> {
                GameService.startGame(gameId);
                return GameService.pauseGame(gameId);
            }, GameState.PAUSED));
    }

    @ParameterizedTest
    @MethodSource("stateChangers")
    public void testStateChange(Function<UUID, Game> stateChanger, GameState state) {
        Game game = GameService.createGame(1);

        Assertions.assertEquals(GameState.CREATED, game.getGameState());

        stateChanger.apply(game.getGameId());

        Assertions.assertEquals(state, game.getGameState());
    }

    @ParameterizedTest
    @MethodSource("stateChangers")
    public void testStateChange_Null_GameId(Function<UUID, Game> stateChanger, GameState state) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> stateChanger.apply(null));
    }

    @Test
    public void testNextGameRound_Progresses_To_Next_Round() {
        Game game = GameService.createGame(2);

        Game result = GameService.nextGameRound(game.getGameId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getRound());
    }

    @Test
    public void testNextGameRound_Null_GameId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.nextGameRound(null));
    }

    @Test
    public void testAddPlayersToGame_Adds_Players() {
        Game game = GameService.createGame(1);
        List<Player> players = Arrays.asList(new Player("subomi"), new Player("nifemi"));

        Game result = GameService.addPlayersToGame(game.getGameId(), players);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getPlayers().size());
    }

    @Test
    public void testAddPlayersToGame_Null_GameId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.addPlayersToGame(null, Arrays.asList(new Player("subomi"))));
    }

    @Test
    public void testAddPlayersToGame_Null_Players() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.addPlayersToGame(UUID.randomUUID(), null));
    }

    @Test
    public void testAddPlayersToGame_Empty_Players() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.addPlayersToGame(UUID.randomUUID(), new ArrayList<Player>()));
    }

    @Test
    public void testAddPlayersToGame_Invalid_Player() {
        List<Player> players = Arrays.asList(new Player("player1"), new Player("player"));

        Assertions.assertThrows(InvalidPlayerExcpetion.class, () -> GameService.addPlayersToGame(UUID.randomUUID(), players));
    }

    @Test
    public void testRemovePlayerFromGame_Removes_Player() {
        Game game = GameService.createGame(1);
        List<Player> players = Arrays.asList(new Player("subomi"), new Player("nifemi"));

        GameService.addPlayersToGame(game.getGameId(), players);
        boolean result = GameService.removePlayerFromGame(game.getGameId(), "subomi");
        
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, game.getPlayers().size());
    }

    @Test
    public void testRemovePlayerFromGame_Null_GameId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.removePlayerFromGame(null, "subomi"));
    }

    @Test
    public void testRemovePlayerFromGame_Null_PlayerName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.removePlayerFromGame(UUID.randomUUID(), null));
    }

    @Test
    public void testRemovePlayerFromGame_Empty_PlayerName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> GameService.removePlayerFromGame(UUID.randomUUID(), ""));
    }

    @Test
    public void testRemovePlayerFromGame_Player_Not_Found() {
        Game game = GameService.createGame(1);
        List<Player> players = Arrays.asList(new Player("subomi"), new Player("nifemi"));

        GameService.addPlayersToGame(game.getGameId(), players);
        boolean result = GameService.removePlayerFromGame(game.getGameId(), "segun");
        
        Assertions.assertFalse(result);
        Assertions.assertEquals(2, game.getPlayers().size());
    }

}
