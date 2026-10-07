package com.jfrog.ztr.tictactoe.game;

import java.util.List;

public class Board {

    private static final int SIZE = 3;

    private static final int[][][] LINES = {
        {{0, 0}, {0, 1}, {0, 2}},
        {{1, 0}, {1, 1}, {1, 2}},
        {{2, 0}, {2, 1}, {2, 2}},
        {{0, 0}, {1, 0}, {2, 0}},
        {{0, 1}, {1, 1}, {2, 1}},
        {{0, 2}, {1, 2}, {2, 2}},
        {{0, 0}, {1, 1}, {2, 2}},
        {{0, 2}, {1, 1}, {2, 0}},
    };

    private final Mark[][] cells = new Mark[SIZE][SIZE];

    public Board() {
        for (Mark[] row : cells) {
            java.util.Arrays.fill(row, Mark.EMPTY);
        }
    }

    public void place(int row, int col, Mark mark) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalMoveException("Cell out of range: (" + row + ", " + col + ")");
        }
        if (cells[row][col] != Mark.EMPTY) {
            throw new IllegalMoveException("Cell already occupied: (" + row + ", " + col + ")");
        }
        cells[row][col] = mark;
    }

    public Board copy() {
        Board copy = new Board();
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(cells[r], 0, copy.cells[r], 0, SIZE);
        }
        return copy;
    }

    public int size() {
        return SIZE;
    }

    public Mark at(int row, int col) {
        return cells[row][col];
    }

    public boolean isFull() {
        for (Mark[] row : cells) {
            for (Mark cell : row) {
                if (cell == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public WinResult findWinner() {
        for (int[][] line : LINES) {
            Mark a = cells[line[0][0]][line[0][1]];
            Mark b = cells[line[1][0]][line[1][1]];
            Mark c = cells[line[2][0]][line[2][1]];
            if (a != Mark.EMPTY && a == b && b == c) {
                return new WinResult(a, List.of(line[0], line[1], line[2]));
            }
        }
        return null;
    }

    public String[][] toSymbolGrid() {
        String[][] grid = new String[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = cells[r][c].symbol();
            }
        }
        return grid;
    }

    public record WinResult(Mark winner, List<int[]> line) {
    }
}
