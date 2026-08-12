package com.subomi.games;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameStorage;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {

    private GameService gameService;
    private IGameStorage gameStorage;
    private IGame game;
    private UUID gameUUID;

    @BeforeEach
    public void setup() {
        this.gameStorage = Mockito.mock(IGameStorage.class);
        this.gameService = new GameService(this.gameStorage);
        this.game = Mockito.mock(IGame.class);
        this.gameUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Mockito
            .when(this.game.getGameId())
            .thenReturn(this.gameUUID);
        Mockito
            .when(this.gameStorage.getGame(this.gameUUID))
            .thenReturn(this.game);
        Mockito.when(this.gameStorage.getGame(this.gameUUID))
            .thenReturn(this.game);
        Mockito.when(this.game.getLeaderboard())
            .thenReturn(new Player[] { new Player("PlayerA"), new Player("PlayerB"), new Player("PlayerC")});
    }

    @Test
    public void testCreateGame_Creates_New_Game() {
        IGame game = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);

        Assertions.assertNotNull(game);
        Assertions.assertEquals(1, game.getNumRounds());
        Assertions.assertEquals(GameState.CREATED, game.getGameState());
        Assertions.assertEquals(-1, game.getRound());
        Assertions.assertEquals(0, game.getPlayers().size());

    
        IGame retrievedGame = this.gameService.getGame(game.getGameId());
        Assertions.assertNotNull(retrievedGame);
        Assertions.assertEquals(game.getGameId(), retrievedGame.getGameId());
    }

    @Test
    public void testCreateGame_Creates_Multiple_Games() {
        IGame game1 = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);
        IGame game2 = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);

        Assertions.assertNotNull(game1);
        Assertions.assertNotNull(game2);

        Assertions.assertNotNull(this.gameService.getGame(game1.getGameId()));
        Assertions.assertNotNull(this.gameService.getGame(game2.getGameId()));
    }

    @Test
    public void testCreateGame_Invalid_Rounds() {
         Assertions.assertThrows(IllegalArgumentException.class, () -> this.gameService.createGame(0, GameType.WHEEL_OF_FORTUNE));
    }

    Stream<Arguments> stateChangers() {
        return Stream.of(
            Arguments.of((Function<UUID, IGame>) (gameId) -> {
                return this.gameService.startGame(gameId);
            }, GameState.STARTED),
            Arguments.of((Function<UUID, IGame>) (gameId) -> {
                this.gameService.startGame(gameId);
                this.gameService.pauseGame(gameId);
                return this.gameService.resumeGame(gameId);
            }, GameState.RESUMED),
            Arguments.of((Function<UUID, IGame>) (gameId) -> {
                this.gameService.startGame(gameId);
                return this.gameService.endGame(gameId);
            }, GameState.ENDED),
            Arguments.of((Function<UUID, IGame>) (gameId) -> {
                this.gameService.startGame(gameId);
                return this.gameService.pauseGame(gameId);
            }, GameState.PAUSED));
    }

    @ParameterizedTest
    @MethodSource("stateChangers")
    public void testStateChange(Function<UUID, IGame> stateChanger, GameState state) {
        IGame game = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);

        Assertions.assertEquals(GameState.CREATED, game.getGameState());

        stateChanger.apply(game.getGameId());

        Assertions.assertEquals(state, game.getGameState());
    }

    @ParameterizedTest
    @MethodSource("stateChangers")
    public void testStateChange_Null_GameId(Function<UUID, IGame> stateChanger, GameState state) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> stateChanger.apply(null));
    }

    @Test
    public void testNextGameRound_Progresses_To_Next_Round() {
        IGame game = this.gameService.createGame(2, GameType.WHEEL_OF_FORTUNE);

        game.start();
        
        IGame result = this.gameService.nextGameRound(game.getGameId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getRound());
    }

    @Test
    public void testNextGameRound_Null_GameId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gameService.nextGameRound(null));
    }

    @Test
    public void testAddPlayersToGame_Adds_Players() {
        IGame game = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);
        List<Player> players = Arrays.asList(new Player("subomi"), new Player("nifemi"));

        List<Player> addedPlayers = this.gameService.addPlayersToGame(game.getGameId(), players);

        Assertions.assertNotNull(addedPlayers);
        Assertions.assertEquals(2, addedPlayers.size());
    }

    @Test
    public void testAddPlayersToGame_Null_GameId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gameService.addPlayersToGame(null, Arrays.asList(new Player("subomi"))));
    }

    @Test
    public void testAddPlayersToGame_Null_Players() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gameService.addPlayersToGame(UUID.randomUUID(), null));
    }

    @Test
    public void testRemovePlayerFromGame_Removes_Player() {
        IGame game = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);
        List<Player> players = Arrays.asList(new Player("subomi"), new Player("nifemi"));

        this.gameService.addPlayersToGame(game.getGameId(), players);
        boolean result = this.gameService.removePlayerFromGame(game.getGameId(), "subomi");
        
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, game.getPlayers().size());
    }

    @Test
    public void testRemovePlayerFromGame_Null_GameId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gameService.removePlayerFromGame(null, "subomi"));
    }

    @Test
    public void testRemovePlayerFromGame_Null_PlayerName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gameService.removePlayerFromGame(UUID.randomUUID(), null));
    }

    @Test
    public void testRemovePlayerFromGame_Player_Not_Found() {
        IGame game = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);
        List<Player> players = Arrays.asList(new Player("subomi"), new Player("nifemi"));

        this.gameService.addPlayersToGame(game.getGameId(), players);
        boolean result = this.gameService.removePlayerFromGame(game.getGameId(), "segun");
        
        Assertions.assertFalse(result);
        Assertions.assertEquals(2, game.getPlayers().size());
    }

    @Test
    public void testGetLeaderboard() {
        IGame game = this.gameService.createGame(1, GameType.WHEEL_OF_FORTUNE);

        Player[] leaderboard = this.gameService.getLeaderboard(game.getGameId());

        Assertions.assertNotNull(leaderboard);
    }

}
