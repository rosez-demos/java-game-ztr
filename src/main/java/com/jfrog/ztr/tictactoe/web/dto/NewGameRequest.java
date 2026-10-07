package com.jfrog.ztr.tictactoe.web.dto;

import com.jfrog.ztr.tictactoe.game.Difficulty;

public record NewGameRequest(Difficulty difficulty) {
}
