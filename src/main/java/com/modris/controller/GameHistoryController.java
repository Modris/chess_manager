package com.modris.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.modris.model.SaveGame;
import com.modris.model.Statistics;
import com.modris.services.ChessService;

import jakarta.validation.Valid;

@RestController
public class GameHistoryController {
	
	private final ChessService chessService;
	
	public GameHistoryController(ChessService chessService) {
		this.chessService = chessService;
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/statistics")
	public Statistics getStats(Authentication auth) {
		return chessService.getStats(auth.getName());
	}

	

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveGame(@Valid SaveGame saveGame, Authentication auth) {
		chessService.saveGame(saveGame, auth.getName());
		
	}
	

	@GetMapping("/get/game/{id}")
	public SaveGame fetchGameWithId(@PathVariable String id) {
		return chessService.getGameWithId(id);
	}


	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getGameHistory")
	public Page<SaveGame> fetchGameHistory(
			@RequestParam(value = "page",required=false, defaultValue = "0") String page,
			@RequestParam(value = "pageSize",required=false, defaultValue = "5") String pageSize,
			Authentication auth) {
		
		return chessService.fetchGameHistory(page, pageSize, auth.getName());
	
	}
	
}
