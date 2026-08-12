package com.subomi.games.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.subomi.games.GameService;
import com.subomi.games.GameType;
import com.subomi.games.Guess;
import com.subomi.games.GuessResult;
import com.subomi.games.Player;
import com.subomi.games.exceptions.InvalidPlayerExcpetion;
import com.subomi.games.interfaces.IGame;
import com.subomi.games.interfaces.IGameService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GameController {

    private IGameService gameService;

    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    public void createGame(HttpServletRequest request, HttpServletResponse response, GameType gameType) {
        String roundsParameter = request.getParameter("rounds");

        if (roundsParameter == null) {
            Helpers.badRequest(response, "Missing parameter: \"rounds\"");
            return;
        }

        int rounds = 0;

        try {
            rounds = Integer.parseInt(roundsParameter);
        }
        catch (NumberFormatException ex) {
            Helpers.badRequest(response, "Invalid rounds parameter");
            return;
        }
        
        IGame game = null;
        try {
            game = this.gameService.createGame(rounds, gameType);
        }
        catch (IllegalArgumentException ex) {
            Helpers.badRequest(response, "Invalid nubmer of rounds.");
            return;
        }

        if (game != null) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            Helpers.writeJsonResponse(response, game);
        } 
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

    }

    public void startGame(HttpServletRequest request, HttpServletResponse response) {
        Helpers.handleGameStateChange(
            request, 
            response, 
            (UUID gameId) -> this.gameService.startGame(gameId));
    }

    public void resumeGame(HttpServletRequest request, HttpServletResponse response) {
        Helpers.handleGameStateChange(
            request, 
            response, 
            (UUID gameId) -> this.gameService.resumeGame(gameId));
    }

    public void endGame(HttpServletRequest request, HttpServletResponse response) {
        Helpers.handleGameStateChange(
            request, 
            response, 
            (UUID gameId) -> this.gameService.endGame(gameId));
    }

    public void pauseGame(HttpServletRequest request, HttpServletResponse response) {
        Helpers.handleGameStateChange(
            request,
            response, 
            (UUID gameId) -> this.gameService.pauseGame(gameId));
    }

    public void getGame(HttpServletRequest request, HttpServletResponse response) {
        UUID gameId = Helpers.getGameId(request);

        if (gameId == null) {
            Helpers.badRequest(response, "Missing \"" + RequestParameters.GAME_ID + "\" request parameter, or invalid value provided.");
            return;
        }

        IGame game = this.gameService.getGame(gameId);

        if (game != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            Helpers.writeJsonResponse(response, game);
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }

    public void nextGameRound(HttpServletRequest request, HttpServletResponse response) {
        Helpers.handleGameStateChange(
            request, 
            response, 
            (UUID gameId) -> this.gameService.nextGameRound(gameId));
    }

    public void addPlayers(HttpServletRequest request, HttpServletResponse response) {
        String body = null;
        List<Player> players = null;
        UUID gameId = Helpers.getGameId(request);

        if (gameId == null) {
            Helpers.badRequest(response, "Missing \"" + RequestParameters.GAME_ID + "\" request parameter, or invalid value provided.");
            return;
        }

        try {
            body = Helpers.getRequestBody(request);
        }
        catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        try {
            players = Helpers.convertJsonToObject(body, new TypeReference<List<Player>>() {});
        }
        catch (JsonProcessingException ex) {
            Helpers.badRequest(response, "Invalid body");
            return;
        }

        List<Player> addedPlayers = null;

        try {
            addedPlayers = this.gameService.addPlayersToGame(gameId, players);
        }
        catch (InvalidPlayerExcpetion ex) {
            Helpers.badRequest(response, "Invalid player");
            return;
        }
        catch (IllegalArgumentException ex) {
            Helpers.badRequest(response, "Invalid gameId or players");
            return;
        }

        if (addedPlayers != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            Helpers.writeJsonResponse(response, addedPlayers);
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }

    public void removePlayer(HttpServletRequest request, HttpServletResponse response) {
        UUID gameId = Helpers.getGameId(request);
        String playerName = Helpers.getPlayerName(request);

        if (playerName == null) {
            Helpers.badRequest(response, "Missing \"" + RequestParameters.PLAYER_NAME + "\" request parameter");
            return;
        }

        boolean isRemoved = false;
        
        try {
            isRemoved = this.gameService.removePlayerFromGame(gameId, playerName);
        }
        catch (IllegalArgumentException ex) {
            Helpers.badRequest(response, "Invalid gameId or playerName");
            return;
        }

        if (isRemoved) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }

    public void getLeaderboard(HttpServletRequest request, HttpServletResponse response) {
        UUID gameId = Helpers.getGameId(request);

        if (gameId == null) {
            Helpers.badRequest(response, "Missing \"" + RequestParameters.GAME_ID + "\" request parameter, or invalid value provided.");
            return;
        }

        Player[] leaderboard = null;
        try {
            leaderboard = this.gameService.getLeaderboard(gameId);
        }
        catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if (leaderboard != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            Helpers.writeJsonResponse(response, leaderboard);
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }
}
