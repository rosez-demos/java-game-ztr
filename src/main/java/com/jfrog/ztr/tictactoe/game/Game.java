package com.jfrog.ztr.tictactoe.game;

import java.util.List;

public class Game {

    private final String id;
    private final Board board = new Board();
    private Mark currentPlayer = Mark.X;
    private GameStatus status = GameStatus.IN_PROGRESS;
    private List<int[]> winningLine;

    public Game(String id) {
        this.id = id;
    }

    public synchronized void applyMove(int row, int col) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalMoveException("Game already finished");
        }
        board.place(row, col, currentPlayer);

        Board.WinResult win = board.findWinner();
        if (win != null) {
            status = win.winner() == Mark.X ? GameStatus.X_WON : GameStatus.O_WON;
            winningLine = win.line();
        } else if (board.isFull()) {
            status = GameStatus.DRAW;
        } else {
            currentPlayer = currentPlayer == Mark.X ? Mark.O : Mark.X;
        }
    }

    public String id() {
        return id;
    }

    public Board board() {
        return board;
    }

    public Mark currentPlayer() {
        return currentPlayer;
    }

    public GameStatus status() {
        return status;
    }

    public List<int[]> winningLine() {
        return winningLine;
    }
}
