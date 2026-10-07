package com.jfrog.ztr.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer {

    private final Random random;

    public ComputerPlayer() {
        this(new Random());
    }

    public ComputerPlayer(Random random) {
        this.random = random;
    }

    public int[] chooseMove(Board board, Mark self, Difficulty difficulty) {
        List<int[]> empty = emptyCells(board);
        if (empty.isEmpty()) {
            throw new IllegalMoveException("No moves available");
        }
        return switch (difficulty) {
            case EASY -> randomMove(empty);
            case MEDIUM -> mediumMove(board, self, empty);
            case HARD -> hardMove(board, self, empty);
        };
    }

    private int[] randomMove(List<int[]> empty) {
        return empty.get(random.nextInt(empty.size()));
    }

    private int[] mediumMove(Board board, Mark self, List<int[]> empty) {
        int[] win = findWinningMove(board, self, empty);
        if (win != null) {
            return win;
        }
        int[] block = findWinningMove(board, opponent(self), empty);
        if (block != null) {
            return block;
        }
        int center = board.size() / 2;
        if (board.at(center, center) == Mark.EMPTY) {
            return new int[] {center, center};
        }
        return randomMove(empty);
    }

    private int[] findWinningMove(Board board, Mark mark, List<int[]> empty) {
        for (int[] cell : empty) {
            Board trial = board.copy();
            trial.place(cell[0], cell[1], mark);
            Board.WinResult win = trial.findWinner();
            if (win != null && win.winner() == mark) {
                return cell;
            }
        }
        return null;
    }

    private int[] hardMove(Board board, Mark self, List<int[]> empty) {
        int bestScore = Integer.MIN_VALUE;
        List<int[]> best = new ArrayList<>();
        for (int[] cell : empty) {
            Board trial = board.copy();
            trial.place(cell[0], cell[1], self);
            int score = minimax(trial, opponent(self), self, 1);
            if (score > bestScore) {
                bestScore = score;
                best.clear();
            }
            if (score == bestScore) {
                best.add(cell);
            }
        }
        return best.get(random.nextInt(best.size()));
    }

    /** Scores the position from {@code self}'s perspective; faster wins and slower losses score higher. */
    private int minimax(Board board, Mark toMove, Mark self, int depth) {
        Board.WinResult win = board.findWinner();
        if (win != null) {
            return win.winner() == self ? 10 - depth : depth - 10;
        }
        if (board.isFull()) {
            return 0;
        }
        boolean maximizing = toMove == self;
        int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int[] cell : emptyCells(board)) {
            Board trial = board.copy();
            trial.place(cell[0], cell[1], toMove);
            int score = minimax(trial, opponent(toMove), self, depth + 1);
            best = maximizing ? Math.max(best, score) : Math.min(best, score);
        }
        return best;
    }

    private static List<int[]> emptyCells(Board board) {
        List<int[]> empty = new ArrayList<>();
        for (int r = 0; r < board.size(); r++) {
            for (int c = 0; c < board.size(); c++) {
                if (board.at(r, c) == Mark.EMPTY) {
                    empty.add(new int[] {r, c});
                }
            }
        }
        return empty;
    }

    private static Mark opponent(Mark mark) {
        return mark == Mark.X ? Mark.O : Mark.X;
    }
}
