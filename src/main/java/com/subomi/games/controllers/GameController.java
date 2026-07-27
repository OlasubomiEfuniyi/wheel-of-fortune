package com.subomi.games.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.subomi.games.Game;
import com.subomi.games.GameService;
import com.subomi.games.Player;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GameController {
    public static void createGame(HttpServletRequest request, HttpServletResponse response) {
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
        }
        
        Game game = GameService.createGame(rounds);

        try {
            response.setStatus(HttpServletResponse.SC_CREATED);
            Helpers.writeJsonResponse(response, game);
        }
        catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void startGame(HttpServletRequest request, HttpServletResponse response) {
        Helpers.HandleGameStateChange(
            request, 
            response, 
            (UUID gameId) -> GameService.startGame(gameId));
    }

    public static void resumeGame(HttpServletRequest request, HttpServletResponse response) {
        Helpers.HandleGameStateChange(
            request, 
            response, 
            (UUID gameId) -> GameService.resumeGame(gameId));
    }

    public static void endGame(HttpServletRequest request, HttpServletResponse response) {
        Helpers.HandleGameStateChange(
            request, 
            response, 
            (UUID gameId) -> GameService.endGame(gameId));
    }

    public static void addPlayers(HttpServletRequest request, HttpServletResponse response) {
        String body = null;
        String gameIdParameter = request.getParameter(RequestParameters.GAME_ID);
        UUID gameId = null;
        List<Player> players = null;

        if (gameIdParameter == null) {
            Helpers.badRequest(response, "Missing \"" + RequestParameters.GAME_ID + "\"");
            return;
        }

        try {
            gameId = UUID.fromString(gameIdParameter);
        }
        catch (IllegalArgumentException ex) {
            Helpers.badRequest(response, "Invalid " + RequestParameters.GAME_ID);
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
        }

        GameService.addPlayersToGame(gameId, players);
    }

    public static void removePlayer(HttpServletRequest request, HttpServletResponse response) {

    }
}
