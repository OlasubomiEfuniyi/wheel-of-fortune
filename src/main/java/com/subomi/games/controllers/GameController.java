package com.subomi.games.controllers;

import java.io.IOException;
import java.util.UUID;

import com.subomi.games.Game;
import com.subomi.games.GameService;
import com.subomi.games.servlets.Helpers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GameController {
    public static void createGame(HttpServletRequest request, HttpServletResponse response) {
        Game game = GameService.createGame();

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

    }

    public static void removePlayer(HttpServletRequest request, HttpServletResponse response) {
        
    }
}
