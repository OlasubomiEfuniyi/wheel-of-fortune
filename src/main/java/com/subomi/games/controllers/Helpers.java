package com.subomi.games.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.subomi.games.interfaces.IGame;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Helpers {
    public static UUID getGameId(HttpServletRequest request) {
        String gameIdParameter = request.getParameter(RequestParameters.GAME_ID);
        UUID gameId = null;

        if (gameIdParameter == null) {
            return null;
        }

        try {
            gameId = UUID.fromString(gameIdParameter);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
        
        return gameId;
    }

    public static String getPlayerName(HttpServletRequest request) {
        return request.getParameter(RequestParameters.PLAYER_NAME);
    }

    public static void handleGameStateChange(HttpServletRequest request, HttpServletResponse response, Function<UUID, IGame> stateChanger) {
        UUID gameId = getGameId(request);

        if (gameId == null) {
            badRequest(response, "Missing \"" + RequestParameters.GAME_ID + "\" request parameter, or invalid value provided.");
            return;
        }

        try {
            IGame game = stateChanger.apply(gameId);
            if (game != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                Helpers.writeJsonResponse(response, game);
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        catch(NoSuchElementException ex) {
            Helpers.badRequest(response, "Game not found");
        }
        catch (IllegalArgumentException ex) {
            Helpers.badRequest(response, "Invalid gameId or playerName");
        }
    }

    public static void writeJsonResponse(HttpServletResponse response, Object obj) {
        try {
            PrintWriter writer = response.getWriter();  
            ObjectMapper objectMapper = new ObjectMapper();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            

            writer.write(objectMapper.writeValueAsString(obj));
            writer.flush();
        }
        catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void writeTextResponse(HttpServletResponse response, String message) throws IOException {
        PrintWriter writer = response.getWriter();  

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        

        writer.write(message);
        writer.flush();
    }

    public static String getRequestBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder bodyBuilder = new StringBuilder();
            String nextLine = reader.readLine();

            while (nextLine != null) {
                bodyBuilder.append(nextLine);
                nextLine = reader.readLine();
            }

            return bodyBuilder.toString();
        }
    }

    public static <T> T convertJsonToObject(String json, TypeReference<T> typeRef) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, typeRef);
    }

    public static void badRequest(HttpServletResponse response, String message) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        try {
            writeTextResponse(response, message);
        }
        catch (IOException ex) {}
    }
}
