package com.jfrog.ztr.tictactoe.game;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public Game createGame() {
        String id = UUID.randomUUID().toString();
        Game game = new Game(id);
        games.put(id, game);
        return game;
    }

    public Game getGame(String id) {
        Game game = games.get(id);
        if (game == null) {
            throw new GameNotFoundException(id);
        }
        return game;
    }

    public Game move(String id, int row, int col) {
        Game game = getGame(id);
        game.applyMove(row, col);
        return game;
    }
}
