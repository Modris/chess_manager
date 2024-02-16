package com.modris.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.modris.Main;
import com.modris.config.SecurityConfig;
import com.modris.model.SaveGame;
import com.modris.services.ChessService;

@ContextConfiguration(classes={Main.class,SecurityConfig.class})
@WebMvcTest(GameHistoryController.class)
@DisplayName("GameHistoryController mapping tests:")
public class GameHistoryControllerTests {

	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	ChessService chessService;
	
/*
	@GetMapping("/get/game/{id}")
	
	
	@GetMapping("/statistics")
	@PostMapping("/save")
	
	@GetMapping("/getGameHistory")
*/
	
	@Test
	@DisplayName("/get/game/{id} Happy flow. Permit all means we can access it.")
	public void getGameIdHappyFlow()  throws Exception{
		when(chessService.getGameWithId("abc")).thenReturn(new SaveGame());
		mockMvc.perform(get("/get/game/abc"))
	      .andExpect(status().isOk());    
	}
	
	@Test
	@DisplayName("/statistics unauthorized 401.")
	public void statisticsUnauhtorized()  throws Exception{
		mockMvc.perform(get("/statistics"))
	      .andExpect(status().isUnauthorized());    
	}
	@Test
	@DisplayName("/getGameHistory unauthorized 401.")
	public void getGameHistoryUnauhtorized()  throws Exception{
		mockMvc.perform(get("/getGameHistory"))
	      .andExpect(status().isUnauthorized());    
	}
	@Test
	@DisplayName("/save unauthorized 401.")
	public void saveUnauhtorized()  throws Exception{
		mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5,b7b6")
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isUnauthorized());    
	}
	
	@Test
	@DisplayName("/save bad request 400. Incorrect color.")
	public void saveBadRequest1()  throws Exception{
		mockMvc.perform(post("/save")
				.param("color", "whites") // white would be correct
				.param("result", "Win")
				.param("moves", "e2e5,b7b6")
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isBadRequest());    
	}
	@Test
	@DisplayName("/save bad request 400. Incorrect result.")
	public void saveBadRequest2()  throws Exception{
		mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Wins") // Win would be correct
				.param("moves", "e2e5,b7b6")
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isBadRequest());    
	}
	@Test
	@DisplayName("/save bad request 400. Incorrect moves.")
	public void saveBadRequest3()  throws Exception{
		mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5, b7b6 ") // spaces
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isBadRequest());    
	}
	
	@Test
	@DisplayName("/save 401 unauthorized with valid constraints.")
	public void saveBadRequest4()  throws Exception{
		mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5,b7b6")
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") // z in the pawn structure
				.param("elo","3190")
				)
	      .andExpect(status().isUnauthorized());    
	}
	
	@Test
	@DisplayName("/save bad request 400. Incorrect elo.")
	public void saveBadRequest5()  throws Exception{
		mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5,b7b6") 
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") 
				.param("elo","3191") // elo is 3191 exceeds 3190 limit.
				)
	      .andExpect(status().isBadRequest());    
	}
	
	
	@Test
	@WithMockUser
	@DisplayName("/save AUTHORIZED. Happy flow. 201 created.")
	public void saveAuthorized201Created()  throws Exception{
		mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5,b7b6") 
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isCreated());    
	}
	
	@Test
	@WithMockUser
	@DisplayName("/getGameHistory AUTHORIZED. 200 OK.")
	public void getGameHistoryAuthorizedOk() throws Exception{
		mockMvc.perform(get("/getGameHistory"))
				.andExpect(status().isOk());
	}
	
	
	@Test
	@WithMockUser
	@DisplayName("/statistics AUTHORIZED. 200 OK.")
	public void statisticsAuthorizedOk() throws Exception{
		mockMvc.perform(get("/statistics"))
				.andExpect(status().isOk());
	}
	
}
