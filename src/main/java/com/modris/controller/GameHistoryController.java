package com.modris.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modris.model.SaveGame;
import com.modris.model.Statistics;
import com.modris.repositories.SaveGameRepository;

@RestController
public class GameHistoryController {
	
	private final SaveGameRepository chessRepository;
	
	public GameHistoryController(SaveGameRepository chessRepository) {
		this.chessRepository = chessRepository;
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/statistics")
	public Statistics getStats(Authentication auth) {

		String answer = chessRepository.gameStatisticsWithUsernameId(auth.getName());
		List<String> answerList = Arrays.asList(answer.split(","));
		Statistics stats = new Statistics();
		if(answerList.size() == 3) {
			int totalWins = Integer.valueOf(answerList.get(0));
			int totalLosses = Integer.valueOf(answerList.get(1));
			int totalDraws = Integer.valueOf(answerList.get(2));
			stats.setTotalWins(totalWins);
			stats.setTotalLosses(totalLosses);
			stats.setTotalDraws(totalDraws);
			return stats;
		}
		return stats;
		
	}


	@PreAuthorize("isAuthenticated()")
	@PostMapping("/save")
	public void saveGame(SaveGame saveGame, Authentication auth) {
		// oidcUser.getPreferredUsername()
		System.out.println("Saving the game into DB...");
		System.out.println(saveGame.toString());
		String result = saveGame.getResult();
		if(result.equals("Draw")) {
			saveGame.setDraws(1);
		} else if(result.equals("Win")) {
			saveGame.setWins(1);
		} else if(result.equals("Loss")) {
			saveGame.setLosses(1);
		}
		String gameID = UUID.randomUUID().toString().replace("-", "");
		saveGame.setGame_id(gameID);
		saveGame.setUsername_id(auth.getName());
		chessRepository.save(saveGame);
	}
	
	@GetMapping("/get/game/{id}")
	public ResponseEntity<SaveGame> fetchGameWithId(@PathVariable String id) {
		SaveGame game = chessRepository.getGameWithId(id); 
		if(game != null) {
			return new ResponseEntity<SaveGame>(game, HttpStatus.OK);
		} else {
			return new ResponseEntity<SaveGame>(HttpStatus.NO_CONTENT); // 204. Request successful but no content
		}
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/getGameHistory")
	public Page<SaveGame> fetchGameHistory(
			@RequestParam(value = "page",required=false) String page,
			@RequestParam(value = "pageSize",required=false) String pageSize,
			Authentication auth) {
		// page, pageSize
		if(page == null || pageSize == null ) {
			page = "0";
			pageSize = "5";
		}

		int pageInt = Integer.valueOf(page);
		int pageSizeInt = Integer.valueOf(pageSize);
		Pageable sortedByIdDesc = PageRequest.of(pageInt, pageSizeInt, Sort.by("id").descending());
		return chessRepository.findPagedGamesWithUsernameId(sortedByIdDesc, auth.getName());
	
	}
	
}
