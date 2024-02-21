package com.modris.services;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.modris.exceptions.FenIsInvalidException;
import com.modris.exceptions.GameNotFoundException;
import com.modris.exceptions.HistoryIsEmptyException;
import com.modris.exceptions.StatisticsNotFoundException;
import com.modris.model.FenValidator;
import com.modris.model.SaveGame;
import com.modris.model.Statistics;
import com.modris.repositories.ChessRepository;

import jakarta.transaction.Transactional;

@Service
public class ChessService {

	private final ChessRepository chessRepository;
	private FenValidator fenValidator;
	
	public ChessService(ChessRepository chessRepository,  FenValidator fenValidator) {
		this.chessRepository = chessRepository;
		this.fenValidator = fenValidator;
	}

	public Statistics getStats(String username_id) {
		String answer = chessRepository.gameStatisticsWithUsernameId(username_id);
		
		List<String> answerList = Arrays.asList(answer.split(","));
		Statistics stats = new Statistics();
	
		if(answerList.size() == 3) {
			int totalWins = Integer.valueOf(answerList.get(0));
			int totalLosses = Integer.valueOf(answerList.get(1));
			int totalDraws = Integer.valueOf(answerList.get(2));
			stats.setTotalWins(totalWins);
			stats.setTotalLosses(totalLosses);
			stats.setTotalDraws(totalDraws);
	
		}
		if(stats.getTotalDraws() ==0 && stats.getTotalLosses() == 0 && stats.getTotalDraws() == 0) {
			throw new StatisticsNotFoundException(username_id);
		}
		return stats;
	}

	@Transactional
	public void saveGame(SaveGame game, String username_id) {

		String result = game.getResult();
		if(result.equals("Draw")) {
			game.setDraws(1);
		} else if(result.equals("Win")) {
			game.setWins(1);
		} else if(result.equals("Loss")) {
			game.setLosses(1);
		}
		String gameID = UUID.randomUUID().toString().replace("-", "");
		game.setGame_id(gameID);
		game.setUsername_id(username_id);

		if(!fenValidator.isFenValid(game.getFen())) {
			throw new FenIsInvalidException(fenValidator.getErrorMessage(), game.getFen());
		}
		chessRepository.save(game);
		
	}

	public SaveGame getGameWithId(String id) {
		SaveGame game = chessRepository.getGameWithId(id); 
		if(game == null) {
			throw new GameNotFoundException(id);
		}
		
		return game;
	}

	public Page<SaveGame> fetchGameHistory(String page, String pageSize, String username_id) {


		if(page == null || pageSize == null ) {
			page = "0";
			pageSize = "5";
		}

		int pageInt = Integer.valueOf(page);
		int pageSizeInt = Integer.valueOf(pageSize);
		Pageable sortedByIdDesc = PageRequest.of(pageInt, pageSizeInt, Sort.by("id").descending());
		Page<SaveGame> history = chessRepository.findPagedGamesWithUsernameId(sortedByIdDesc, username_id);
	
		if(history.isEmpty()) {
			throw new HistoryIsEmptyException(username_id);
		}
		System.out.println(history);
		return history;
		
	}



}
