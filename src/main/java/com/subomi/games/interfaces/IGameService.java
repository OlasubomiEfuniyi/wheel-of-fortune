package com.subomi.games.interfaces;

import java.util.List;
import java.util.UUID;

import com.subomi.games.Player;

public interface IGameService<T extends IGame> {
    public T createGame(int numRounds);

    public T startGame(UUID gameId);

    public T resumeGame(UUID gameId);

    public T endGame(UUID gameId);

    public T pauseGame(UUID gameId);

    public T nextGameRound(UUID gameId);

    public T getGame(UUID gameId); 

    public List<Player> addPlayersToGame(UUID gameId, List<Player> players);

    public boolean removePlayerFromGame(UUID gameId, String playerName);

    public Player[] getLeaderboard(UUID gameId);
}
