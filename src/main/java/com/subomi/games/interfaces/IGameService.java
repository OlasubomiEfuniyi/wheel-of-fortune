package com.subomi.games.interfaces;

import java.util.List;
import java.util.UUID;

import com.subomi.games.GameType;
import com.subomi.games.Player;

public interface IGameService {
    public IGame createGame(int rounds, GameType gameType);

    public IGame startGame(UUID gameId);

    public IGame resumeGame(UUID gameId);

    public IGame endGame(UUID gameId);

    public IGame pauseGame(UUID gameId);

    public IGame nextGameRound(UUID gameId);

    public IGame getGame(UUID gameId); 

    public List<Player> addPlayersToGame(UUID gameId, List<Player> players);

    public boolean removePlayerFromGame(UUID gameId, String playerName);

    public Player[] getLeaderboard(UUID gameId);
}
