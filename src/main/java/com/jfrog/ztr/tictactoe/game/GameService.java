package com.jfrog.ztr.tictactoe.game;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private static final Mark COMPUTER_MARK = Mark.O;

    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final ComputerPlayer computerPlayer = new ComputerPlayer();

    public Game createGame() {
        return createGame(null);
    }

    public Game createGame(Difficulty difficulty) {
        String id = UUID.randomUUID().toString();
        Game game = new Game(id, difficulty);
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
        synchronized (game) {
            game.applyMove(row, col);
            if (game.difficulty() != null
                    && game.status() == GameStatus.IN_PROGRESS
                    && game.currentPlayer() == COMPUTER_MARK) {
                int[] reply = computerPlayer.chooseMove(game.board(), COMPUTER_MARK, game.difficulty());
                game.applyMove(reply[0], reply[1]);
            }
        }
        return game;
    }
}
