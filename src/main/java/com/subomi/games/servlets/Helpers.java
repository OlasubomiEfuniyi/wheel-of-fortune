package com.subomi.games.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subomi.games.Game;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Helpers {
    public static void HandleGameStateChange(HttpServletRequest request, HttpServletResponse response, Function<UUID, Game> stateChanger) {
        UUID gameId = UUID.fromString(request.getParameter("gameId"));

        try {
            Game game = stateChanger.apply(gameId);
            if (game != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                Helpers.writeJsonResponse(response, game);
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        catch(NoSuchElementException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static void writeJsonResponse(HttpServletResponse response, Object obj) throws IOException {
        PrintWriter writer = response.getWriter();  
        ObjectMapper objectMapper = new ObjectMapper();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        

        writer.write(objectMapper.writeValueAsString(obj));
        writer.flush();
    }

    public static void writeTextResponse(HttpServletResponse response, String message) throws IOException {
        PrintWriter writer = response.getWriter();  

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        

        writer.write(message);
        writer.flush();
    }

    public static void badRequest(HttpServletResponse response, String message) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        try {
            writeTextResponse(response, message);
        }
        catch (IOException ex) {}
    }
}
