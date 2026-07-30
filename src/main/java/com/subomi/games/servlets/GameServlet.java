package com.subomi.games.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.subomi.games.controllers.GameController;
import com.subomi.games.controllers.Helpers;

@WebServlet(urlPatterns = {"/game/*"})
public class GameServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();

        switch(path) {
            case "/game":
                GameController.getGame(request, response);
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
            case "/game/create":
                GameController.createGame(request, response);
                break;
            case "/game/nextRound":
                GameController.nextGameRound(request, response);
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
            case "/game/start":
                GameController.startGame(request, response);
                break;
            case "/game/resume":
                GameController.resumeGame(request, response);
                break;
            case "/game/end":
                GameController.endGame(request, response);
                break;
            case "/game/pause/":
                GameController.pauseGame(request, response);
                break;
            case "/game/addPlayers":
                GameController.addPlayers(request, response);
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
            case "/game/removePlayer":
                GameController.removePlayer(request, response);
                break;
            default:
                Helpers.badRequest(response, badPath(path));
        }
    }

    private String badPath(String path) {
        return "The provided path does not exist: " + path;
    }



}
