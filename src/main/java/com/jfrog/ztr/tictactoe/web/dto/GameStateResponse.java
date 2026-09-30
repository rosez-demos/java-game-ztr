package com.jfrog.ztr.tictactoe.web.dto;

import com.jfrog.ztr.tictactoe.game.Game;
import java.util.List;

public record GameStateResponse(
        String id,
        String[][] board,
        String currentPlayer,
        String status,
        List<int[]> winningLine) {

    public static GameStateResponse from(Game game) {
        return new GameStateResponse(
                game.id(),
                game.board().toSymbolGrid(),
                game.currentPlayer().symbol(),
                game.status().name(),
                game.winningLine());
    }
}
