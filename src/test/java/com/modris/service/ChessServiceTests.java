package com.modris.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.modris.TestContainersBase;
import com.modris.exceptions.FenIsInvalidException;
import com.modris.exceptions.GameNotFoundException;
import com.modris.exceptions.HistoryIsEmptyException;
import com.modris.exceptions.StatisticsNotFoundException;
import com.modris.model.SaveGame;
import com.modris.model.Statistics;
import com.modris.services.ChessService;

import jakarta.transaction.Transactional;

public class ChessServiceTests extends TestContainersBase{
	
	@Autowired
	ChessService chessService;
	
	
	@Test
	@DisplayName("Precheck data. Init script for custom docker image should contain data.")
	void start() {
		Page<SaveGame> pagedGames = chessService.fetchGameHistory("0","5","da903091-e26b-458c-84ef-fd1945359f35");
		List<SaveGame> games = pagedGames.getContent();
		assertEquals(2, games.size(), ()-> "2 games with this ID should be in the init script.");		
		
		SaveGame game = chessService.getGameWithId("155131bf169e43e59898bcf098ef8795");
	
		assertAll(
				() -> assertEquals(0, game.getWins()),
				() -> assertEquals(1, game.getLosses()),
				() -> assertEquals(0, game.getDraws()),
				() -> assertEquals("1a309832-7e10-47e8-8d56-1cf01da1198b", game.getUsername_id()),
				() -> assertEquals("2834", game.getElo()),
				() -> assertEquals("white", game.getColor()),
				() -> assertEquals("Black is victorious", game.getWinner()),
				() -> assertEquals("rnb1kbnr/pp2pppp/8/1qpp4/8/K2P4/PPP1PPPP/RNBQ1BNR b kq - 9 7", game.getFen()),
				() -> assertEquals("d2d3,d7d5,e1d2,c7c5,d2c3,d8a5,c3b3,a5b6,b3a4,b6a5,a4b3,a5b5,b3a3,b5b4", game.getMoves())
				
				);
		
	}
	
	@Test
	@Transactional
	@DisplayName("/save Happy flow. Game successfully saved into Database.")
	public void saveGameHappyFlow() {
		var saveThisGame = new SaveGame("Black resigns.Test","3000","black","h2h1,h2h1n"
				,"2r5/8/8/3R4/2P1k3/2K5/8/8 b - - 0 1","Win");
		
		chessService.saveGame(saveThisGame, "username_id_test");
		
		Page<SaveGame> pagedGame = chessService.fetchGameHistory("0", "5", "username_id_test");
		List<SaveGame> fetchedGame = pagedGame.getContent();
		
		
		assertAll(
				() -> assertEquals(1, fetchedGame.size()),
				() -> assertEquals(1, fetchedGame.get(0).getWins()),
				() -> assertEquals(0, fetchedGame.get(0).getDraws()),
				() -> assertEquals(0, fetchedGame.get(0).getLosses()),
				() -> assertEquals("3000", fetchedGame.get(0).getElo()),
				() -> assertEquals("black", fetchedGame.get(0).getColor()),
				() -> assertEquals("2r5/8/8/3R4/2P1k3/2K5/8/8 b - - 0 1", fetchedGame.get(0).getFen()),
				() -> assertEquals("h2h1,h2h1n", fetchedGame.get(0).getMoves())
				
				);
		
	}
	
	@Test
	@Transactional // to roll back data between tests.
	@DisplayName("getStats Happy flow. Statistics successfully fetched from the database.")
	public void getStatsHappyFlow() {
		Statistics answer = chessService.getStats("da903091-e26b-458c-84ef-fd1945359f35");

		assertAll(
				() -> assertEquals(0, answer.getTotalWins()),
				() -> assertEquals(2, answer.getTotalLosses()),
				() -> assertEquals(0, answer.getTotalDraws())
				);
		
	}
	
	@Test
	@Transactional // to roll back data between tests.
	@DisplayName("getGameWithId Happy flow. Game successfully fetched from the database.")
	public void getGameWithIdHappyFlow() {
		SaveGame game = chessService.getGameWithId("b59ce5bd025e4e28acf2ba7ee1d41d74");
		
		assertAll(
				() -> assertEquals(0, game.getWins()),
				() -> assertEquals(1, game.getLosses()),
				() -> assertEquals(0, game.getDraws()),
				() -> assertEquals("da903091-e26b-458c-84ef-fd1945359f35", game.getUsername_id()),
				() -> assertEquals("1770", game.getElo()),
				() -> assertEquals("black", game.getColor()),
				() -> assertEquals("Black resigns.", game.getWinner()),
				() -> assertEquals("rn1qkbnr/pbpppppp/1p6/8/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 1 3", game.getFen()),
				() -> assertEquals("d2d4,b7b6,e2e4,c8b7,d4d5", game.getMoves())
				
				);
		
	}
	
	@Test
	@Transactional // to roll back data between tests.
	@DisplayName("fetchGameHistory Happy flow. History successfully fetched from the database. Sorting check. ID descending.")
	public void fetchGameHistory() {
		Page<SaveGame> pagedGames = chessService.fetchGameHistory("0","5","da903091-e26b-458c-84ef-fd1945359f35");
		List<SaveGame> games = pagedGames.getContent();

	
		assertAll(
				() -> assertEquals(0, games.get(0).getWins()),
				() -> assertEquals(1, games.get(0).getLosses()),
				() -> assertEquals(0, games.get(0).getDraws()),
				() -> assertEquals("da903091-e26b-458c-84ef-fd1945359f35",games.get(0).getUsername_id()),
				() -> assertEquals("2389", games.get(0).getElo()),
				() -> assertEquals("black",games.get(0).getColor()),
				() -> assertEquals("White is victorious", games.get(0).getWinner()),
				() -> assertEquals("2b1r2Q/4qk2/6R1/r2Pp1P1/P1p1B3/5P2/1P6/1K6 w - - 5 43", games.get(0).getFen()),
				() -> assertEquals("e2e4,b7b6,d2d4,c8b7,f1d3,d7d6,g1f3,b8d7,c2c4,g7g6,h2h3,f8g7,b1c3,g8f6,c1e3,e8g8,d3c2,e7e6,a2a4,c7c5,d4d5,e6d5,e4d5,d7e5,f3e5,d6e5,g2g4,f6d7,c3e4,d8c7,d1e2,d7f6,e4f6,g7f6,e1c1,f8e8,g4g5,f6g7,c2e4,a7a6,h3h4,b6b5,h4h5,b5c4,e2c4,a6a5,c1b1,a8c8,d1c1,g7f8,f2f3,c7d6,e3d2,b7a6,c4c2,a6e2,h1h2,e2a6,c1h1,c5c4,h5g6,f7g6,h2h7,f8g7,d2c3,c8c7,h1h6,a6c8,c3a5,c7a7,h6g6,d6e7,c2h2,a7a5,h2h5,g8f8,h7h8,g7h8,h5h8,f8f7,h8h7,f7f8,h7h8,f8f7,g6g7", games.get(0).getMoves())
				
				);
		
	}
	
	
	@Test
	@Transactional // to roll back data between tests.
	@DisplayName("getStats Empty Statistics throws StatisticsNotFoundException.")
	public void getStatsExceptionFlow() {
		
		StatisticsNotFoundException exception = assertThrows(
                StatisticsNotFoundException.class,
                () -> chessService.getStats("ooga-booga")
        );

        assertEquals("Statistics for user id: `ooga-booga` not found.", exception.getMessage());
		
	}
	
	@Test
	@Transactional 
	@DisplayName("saveGame invalid Fen throws FenIsInvalidException.")
	public void saveGameInvalidFenThrowsException() {
		var saveThisGame = new SaveGame("Black resigns.Test","3000","black","h2h1,h2h1n"
				,"2r5/8/8/3R4/2zzzP1k3/2K5/8/8 b - - 0 1","Win");
		FenIsInvalidException exception = assertThrows(
				FenIsInvalidException.class,
				() -> chessService.saveGame(saveThisGame,"username_id")
        );

        assertEquals("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.. Fen: 2r5/8/8/3R4/2zzzP1k3/2K5/8/8 b - - 0 1", exception.getMessage());
		
	}

	@Test
	@Transactional 
	@DisplayName("getGameWithId invalid game id throws GameNotFoundException.")
	public void getGameWithIdThrowsException() {
		
		GameNotFoundException exception = assertThrows(
				GameNotFoundException.class,
				() -> chessService.getGameWithId("ooga-booga2")
        );

        assertEquals("A book with game id: `ooga-booga2` not found.", exception.getMessage());
		
	}
	
	// fetchGameHistory
	
	@Test
	@Transactional 
	@DisplayName("fetchGameHistory empty history for ID throws HistoryIsEmptyException.")
	public void fetchGameHistoryThrowsException() {
		
		HistoryIsEmptyException exception = assertThrows(
				HistoryIsEmptyException.class,
				() -> chessService.fetchGameHistory("0","5","ooga-booga2")
        );

        assertEquals("History for user id `ooga-booga2` is empty.", exception.getMessage());
		
	}
	
	
}
