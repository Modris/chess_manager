package com.modris.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.modris.Main;
import com.modris.config.SecurityConfig;
import com.modris.exceptions.FenIsInvalidException;
import com.modris.exceptions.GameNotFoundException;
import com.modris.exceptions.HistoryIsEmptyException;
import com.modris.exceptions.StatisticsNotFoundException;
import com.modris.model.FenValidator;
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
	
	@MockBean
	FenValidator fenValidator;
	
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
		MvcResult result = mockMvc.perform(post("/save")
				.param("color", "whites") // white would be correct
				.param("result", "Win")
				.param("moves", "e2e5,b7b6")
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isBadRequest()).andReturn();    
		assertTrue(result.getResolvedException().getMessage().contains("must match \"black|white\""));
		
	}
	@Test
	@DisplayName("/save bad request 400. Incorrect result.")
	public void saveBadRequest2()  throws Exception{
		MvcResult result = mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Wins") // Win would be correct
				.param("moves", "e2e5,b7b6")
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isBadRequest()).andReturn();    
		assertTrue(result.getResolvedException().getMessage().contains("must match \"Draw|Win|Loss\""));
	}
	@Test
	@DisplayName("/save bad request 400. Incorrect moves.")
	public void saveBadRequest3()  throws Exception{
		MvcResult result = mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5, b7b6 ") // spaces
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
				.param("elo","3190")
				)
	      .andExpect(status().isBadRequest()).andReturn();    

		assertTrue(result.getResolvedException().getMessage().contains("'moves': rejected value [e2e5, b7b6 ]"));
		assertTrue(result.getResolvedException().getMessage().contains("must match \"([abcdefgh][1-8][abcdefgh][1-8][qnbr]?,?)+\""));
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
	@DisplayName("/save bad request 400. Incorrect elo. Exceeds Maximum 3190.")
	public void saveBadRequest5()  throws Exception{
		MvcResult result = mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5,b7b6") 
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") 
				.param("elo","3191") // elo is 3191 exceeds 3190 limit.
				)
	      .andExpect(status().isBadRequest()).andReturn();    
	
		assertTrue(result.getResolvedException().getMessage().contains("Elo must be at most 3190"));
	}
	
	@Test
	@DisplayName("/save bad request 400. Incorrect elo. Exceeds minimum 1320.")
	public void saveBadRequest6()  throws Exception{
		MvcResult result = mockMvc.perform(post("/save")
				.param("color", "white")
				.param("result", "Win")
				.param("moves", "e2e5,b7b6") 
				.param("fen", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") 
				.param("elo","1319") // elo is 1319 below minium.
				)
	      .andExpect(status().isBadRequest()).andReturn();    
	
		
		assertTrue(result.getResolvedException().getMessage().contains("Elo must be at least 1320"));
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
	
	@Test
	@WithMockUser(username = "abc")
	@DisplayName("/statistics AUTHORIZED. 204 no content throws StatisticsNotFoundException.")
	public void statisticsAuthorizedNoContent() throws Exception{
		//throw new StatisticsNotFoundException("abc");
		when(chessService.getStats("abc")).thenThrow( new StatisticsNotFoundException("abc"));
		MvcResult result = mockMvc.perform(get("/statistics"))
				.andExpect(status().isNoContent()).andReturn();
		
		Optional<StatisticsNotFoundException> someException = Optional.ofNullable((StatisticsNotFoundException) result.getResolvedException());
		assertEquals("Statistics for user id: `abc` not found.", result.getResolvedException().getMessage());

	}
	
	@Test
	@WithMockUser(username = "abc")
	@DisplayName("/getGameHistory AUTHORIZED. 204 no content throws HistoryIsEmptyException.")
	public void getGameHistoryNoContent() throws Exception{
		//throw new StatisticsNotFoundException("abc");
		when(chessService.fetchGameHistory("0", "5","abc")).thenThrow( new HistoryIsEmptyException("abc"));
		MvcResult result = mockMvc.perform(get("/getGameHistory")
				.param("page", "0")
				.param("pageSize", "5"))
				.andExpect(status().isNoContent()).andReturn();
		
		Optional<HistoryIsEmptyException> someException = Optional.ofNullable((HistoryIsEmptyException) result.getResolvedException());
		assertEquals("History for user id `abc` is empty.", result.getResolvedException().getMessage());

	}
	
	@Test
	@WithMockUser(username = "abc")
	@DisplayName("/get/game/abc AUTHORIZED. 204 no content throws GameNotFoundException.")
	public void getGameIdAuthorized204NoContent() throws Exception{
		//throw new StatisticsNotFoundException("abc");
		when(chessService.getGameWithId("abc")).thenThrow( new GameNotFoundException("abc"));
		MvcResult result = mockMvc.perform(get("/get/game/abc"))
				.andExpect(status().isNoContent()).andReturn();
		
		Optional<GameNotFoundException> someException = Optional.ofNullable((GameNotFoundException) result.getResolvedException());
		assertEquals("A book with game id: `abc` not found.", result.getResolvedException().getMessage());

	}
	// FenIsInvalidException
	
	@Test
	@WithMockUser(username = "abc")
	@DisplayName("/save AUTHORIZED. Incorrect FEN throws FenIsInvalidException (400 bad request)")
	public void saveAuthorizedIncorrectFenThrowsException() throws Exception{

		
		var game = new SaveGame("Black resigns.","3189","white","h2h3,"
				,"invalidFEN","Win");

		doThrow(new FenIsInvalidException("Invalid Fen.", game.getFen()))
        .when(chessService).saveGame(any(SaveGame.class), any(String.class));
		
		MvcResult result = mockMvc.perform(post("/save")
				.param("result", game.getResult())
				.param("elo", game.getElo())
				.param("color", game.getColor())
				.param("moves", game.getMoves())
				.param("fen", game.getFen())
				.param("winner", game.getWinner()))
				.andExpect(status().isBadRequest()).andReturn();
		
		
		Optional<FenIsInvalidException> someException = Optional.ofNullable((FenIsInvalidException) result.getResolvedException());
		assertEquals("Invalid Fen.. Fen: invalidFEN", result.getResolvedException().getMessage());

	}
	
}
