package com.jfrog.ztr.tictactoe.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jfrog.ztr.tictactoe.game.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GameController.class)
@Import({GameService.class, GameExceptionHandler.class})
class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void newGameWithoutBodyIsTwoPlayer() throws Exception {
        mvc.perform(post("/api/game/new"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.difficulty").doesNotExist());
    }

    @Test
    void newGameWithDifficulty() throws Exception {
        mvc.perform(post("/api/game/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"difficulty\":\"HARD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.difficulty").value("HARD"));
    }

    @Test
    void invalidDifficultyIsBadRequest() throws Exception {
        mvc.perform(post("/api/game/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"difficulty\":\"IMPOSSIBLE\"}"))
                .andExpect(status().isBadRequest());
    }
}
