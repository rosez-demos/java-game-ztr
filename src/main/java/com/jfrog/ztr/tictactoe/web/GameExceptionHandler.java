package com.jfrog.ztr.tictactoe.web;

import com.jfrog.ztr.tictactoe.game.GameNotFoundException;
import com.jfrog.ztr.tictactoe.game.IllegalMoveException;
import com.jfrog.ztr.tictactoe.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(GameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalMoveException.class)
    public ResponseEntity<ErrorResponse> handleIllegalMove(IllegalMoveException ex) {
        HttpStatus status = ex.getMessage().contains("out of range")
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage()));
    }
}
