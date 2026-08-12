package com.subomi.games.controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.subomi.games.GameType;
import com.subomi.games.Guess;
import com.subomi.games.GuessResult;
import com.subomi.games.interfaces.IWheelOfFortuneGameService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WheelOfFortuneGameController extends GameController {
    private IWheelOfFortuneGameService gameService;

    public WheelOfFortuneGameController(IWheelOfFortuneGameService gameService) {
        super(gameService);
    }

    public void createGame(HttpServletRequest request, HttpServletResponse response) {
        super.createGame(request, response, GameType.WHEEL_OF_FORTUNE);
    }

    public void recordPlayerGuess(HttpServletRequest request, HttpServletResponse response) {
        String body = null;
        Guess guess = null;

        try {
            body = Helpers.getRequestBody(request);
        }
        catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        try {
            guess = Helpers.convertJsonToObject(body, new TypeReference<Guess>() {});
        }
        catch (JsonProcessingException ex) {
            Helpers.badRequest(response, "Invalid body");
            return;
        }

        GuessResult result = null;
        try {
            result = this.gameService.recordPlayerGuess(guess);
        }
        catch(IllegalArgumentException ex) {
            Helpers.badRequest(response, "Invalid Guess JSON");
            return;
        }

        if (result != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            Helpers.writeJsonResponse(response, result);
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }
}
