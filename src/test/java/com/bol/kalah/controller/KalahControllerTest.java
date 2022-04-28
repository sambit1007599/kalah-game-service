package com.bol.kalah.controller;

import com.bol.kalah.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
public class KalahControllerTest {

    protected MockMvc mvc;

    @Mock
    private GameService gameService;

    @InjectMocks
    private KalahController controller;

    @BeforeEach
    public void setUp() {

        this.mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testCreateGame() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/games")).andExpect(status().isCreated());
    }

    @Test
    public void testPlayTheGame() throws Exception {

        mvc.perform(MockMvcRequestBuilders.put("/games/{game-id}/pits/{pit-index}", 1L, 1))
                .andExpect(status().isOk());

    }

}
