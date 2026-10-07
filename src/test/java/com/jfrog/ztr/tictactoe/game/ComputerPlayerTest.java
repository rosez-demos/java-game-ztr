package com.jfrog.ztr.tictactoe.game;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;

class ComputerPlayerTest {

    private final ComputerPlayer player = new ComputerPlayer(new Random(42));

    @Test
    void easyAlwaysPicksEmptyCell() {
        Board board = new Board();
        board.place(0, 0, Mark.X);
        board.place(1, 1, Mark.O);
        for (int i = 0; i < 50; i++) {
            int[] move = player.chooseMove(board, Mark.O, Difficulty.EASY);
            assertEquals(Mark.EMPTY, board.at(move[0], move[1]));
        }
    }

    @Test
    void mediumTakesImmediateWin() {
        Board board = new Board();
        board.place(0, 0, Mark.O);
        board.place(0, 1, Mark.O);
        board.place(1, 0, Mark.X);
        board.place(1, 1, Mark.X);
        assertArrayEquals(new int[] {0, 2}, player.chooseMove(board, Mark.O, Difficulty.MEDIUM));
    }

    @Test
    void mediumBlocksOpponentWin() {
        Board board = new Board();
        board.place(0, 0, Mark.X);
        board.place(0, 1, Mark.X);
        board.place(1, 1, Mark.O);
        assertArrayEquals(new int[] {0, 2}, player.chooseMove(board, Mark.O, Difficulty.MEDIUM));
    }

    @Test
    void hardNeverLosesAgainstAnyHumanPlay() {
        assertNoHumanWin(new Board());
    }

    /** Human (X) tries every legal move at every turn; hard computer (O) replies. X must never win. */
    private void assertNoHumanWin(Board board) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.at(r, c) != Mark.EMPTY) {
                    continue;
                }
                Board next = board.copy();
                next.place(r, c, Mark.X);
                Board.WinResult win = next.findWinner();
                if (win != null) {
                    assertNotEquals(Mark.X, win.winner(), "human won");
                    continue;
                }
                if (next.isFull()) {
                    continue;
                }
                int[] reply = player.chooseMove(next, Mark.O, Difficulty.HARD);
                next.place(reply[0], reply[1], Mark.O);
                win = next.findWinner();
                if (win != null || next.isFull()) {
                    continue;
                }
                assertNoHumanWin(next);
            }
        }
    }
}
