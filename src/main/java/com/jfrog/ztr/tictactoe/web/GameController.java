package com.jfrog.ztr.tictactoe.web;

import com.jfrog.ztr.tictactoe.game.Game;
import com.jfrog.ztr.tictactoe.game.GameService;
import com.jfrog.ztr.tictactoe.web.dto.GameStateResponse;
import com.jfrog.ztr.tictactoe.web.dto.MoveRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/api/game/new")
    @ResponseStatus(HttpStatus.CREATED)
    public GameStateResponse newGame() {
        Game game = gameService.createGame();
        return GameStateResponse.from(game);
    }

    @GetMapping("/api/game/{id}")
    public GameStateResponse getGame(@PathVariable String id) {
        return GameStateResponse.from(gameService.getGame(id));
    }

    @PostMapping("/api/game/{id}/move")
    public GameStateResponse move(@PathVariable String id, @RequestBody MoveRequest request) {
        Game game = gameService.move(id, request.row(), request.col());
        return GameStateResponse.from(game);
    }
}
