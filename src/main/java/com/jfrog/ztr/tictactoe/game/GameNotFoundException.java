package com.jfrog.ztr.tictactoe.game;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String gameId) {
        super("No game found with id: " + gameId);
    }
}
