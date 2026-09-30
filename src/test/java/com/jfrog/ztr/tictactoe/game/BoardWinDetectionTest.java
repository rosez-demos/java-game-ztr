package com.jfrog.ztr.tictactoe.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BoardWinDetectionTest {

    @Test
    void detectsRowWin() {
        Board board = new Board();
        board.place(0, 0, Mark.X);
        board.place(1, 0, Mark.O);
        board.place(0, 1, Mark.X);
        board.place(1, 1, Mark.O);
        board.place(0, 2, Mark.X);

        Board.WinResult result = board.findWinner();

        assertThat(result).isNotNull();
        assertThat(result.winner()).isEqualTo(Mark.X);
    }

    @Test
    void detectsColumnWin() {
        Board board = new Board();
        board.place(0, 0, Mark.O);
        board.place(0, 1, Mark.X);
        board.place(1, 0, Mark.O);
        board.place(1, 1, Mark.X);
        board.place(2, 0, Mark.O);

        Board.WinResult result = board.findWinner();

        assertThat(result).isNotNull();
        assertThat(result.winner()).isEqualTo(Mark.O);
    }

    @Test
    void detectsDiagonalWin() {
        Board board = new Board();
        board.place(0, 0, Mark.X);
        board.place(0, 1, Mark.O);
        board.place(1, 1, Mark.X);
        board.place(0, 2, Mark.O);
        board.place(2, 2, Mark.X);

        Board.WinResult result = board.findWinner();

        assertThat(result).isNotNull();
        assertThat(result.winner()).isEqualTo(Mark.X);
    }

    @Test
    void detectsDraw() {
        Board board = new Board();
        // X O X
        // X O O
        // O X X
        board.place(0, 0, Mark.X);
        board.place(0, 1, Mark.O);
        board.place(0, 2, Mark.X);
        board.place(1, 0, Mark.X);
        board.place(1, 1, Mark.O);
        board.place(1, 2, Mark.O);
        board.place(2, 0, Mark.O);
        board.place(2, 1, Mark.X);
        board.place(2, 2, Mark.X);

        assertThat(board.findWinner()).isNull();
        assertThat(board.isFull()).isTrue();
    }

    @Test
    void rejectsMoveOnOccupiedCell() {
        Board board = new Board();
        board.place(0, 0, Mark.X);

        assertThatThrownBy(() -> board.place(0, 0, Mark.O))
                .isInstanceOf(IllegalMoveException.class);
    }

    @Test
    void rejectsOutOfRangeMove() {
        Board board = new Board();

        assertThatThrownBy(() -> board.place(3, 0, Mark.X))
                .isInstanceOf(IllegalMoveException.class);
    }
}
