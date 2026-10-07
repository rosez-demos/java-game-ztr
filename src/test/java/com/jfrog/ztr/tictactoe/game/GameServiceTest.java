package com.jfrog.ztr.tictactoe.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class GameServiceTest {

    private final GameService service = new GameService();

    private static int countMarks(Game game) {
        int n = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.board().at(r, c) != Mark.EMPTY) {
                    n++;
                }
            }
        }
        return n;
    }

    @Test
    void twoPlayerModeDoesNotAutoReply() {
        Game game = service.createGame();
        assertNull(game.difficulty());
        service.move(game.id(), 0, 0);
        assertEquals(1, countMarks(game));
        assertEquals(Mark.O, game.currentPlayer());
    }

    @Test
    void computerRepliesAfterHumanMove() {
        Game game = service.createGame(Difficulty.HARD);
        service.move(game.id(), 0, 0);
        assertEquals(2, countMarks(game));
        assertEquals(Mark.X, game.currentPlayer());
    }

    @Test
    void computerDoesNotMoveAfterHumanWins() {
        Game game = service.createGame(Difficulty.EASY);
        // Force a human win by placing marks directly: X wins on top row.
        game.applyMove(0, 0); // X
        game.applyMove(1, 0); // O
        game.applyMove(0, 1); // X
        game.applyMove(1, 1); // O
        service.move(game.id(), 0, 2); // X wins
        assertEquals(GameStatus.X_WON, game.status());
        assertEquals(5, countMarks(game));
    }
}
