package com.subomi.games.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.subomi.games.GameStorage;
import com.subomi.games.WheelOfFortuneGameService;
import com.subomi.games.controllers.Helpers;
import com.subomi.games.controllers.WheelOfFortuneGameController;

@WebServlet(urlPatterns = {"/wof/*"})
public class WheelOfFortuneServlet extends HttpServlet {
    private WheelOfFortuneGameController controller;

    public WheelOfFortuneServlet() {
        this.controller = new WheelOfFortuneGameController(new WheelOfFortuneGameService(new GameStorage()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();

        switch(path) {
            case "/wof/":
                controller.getGame(request, response);
                break;
            case "/wof/leaderboard":
                controller.getLeaderboard(request, response);
                break;
            default:
                Helpers.badRequest(response, badPath(path));
                break;
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();

        switch (path) {
            case "/wof/create":
                controller.createGame(request, response);
                break;
            case "/wof/nextRound":
                controller.nextGameRound(request, response);
                break;
            case "/wof/guess":
                controller.recordPlayerGuess(request, response);
                break;
            default:
                Helpers.badRequest(response, badPath(path));
                break;
        }
        
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();

        switch (path) {
            case "/wof/start":
                controller.startGame(request, response);
                break;
            case "/wof/resume":
                controller.resumeGame(request, response);
                break;
            case "/wof/end":
                controller.endGame(request, response);
                break;
            case "/wof/pause/":
                controller.pauseGame(request, response);
                break;
            case "/wof/addPlayers":
                controller.addPlayers(request, response);
                break;
            default:
                Helpers.badRequest(response, badPath(path));
                break;
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();

        switch (path) {
            case "/wof/removePlayer":
                controller.removePlayer(request, response);
                break;
            default:
                Helpers.badRequest(response, badPath(path));
        }
    }

    private String badPath(String path) {
        return "The provided path does not exist: " + path;
    }



}
