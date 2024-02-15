package com.modris.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.modris.model.SaveGame;


public interface SaveGameRepository extends JpaRepository<SaveGame,Long> {

	@Query("SELECT COUNT(CASE WHEN s.wins = 1 THEN 1 END), " +
		       "COUNT(CASE WHEN s.losses = 1 THEN 1 END), " +
		       "COUNT(CASE WHEN s.draws = 1 THEN 1 END) " +
		       "FROM SaveGame s WHERE s.username_id = :username_id")
	public String gameStatisticsWithUsernameId(@Param("username_id") String username_id);
	
	@Query("SELECT s FROM SaveGame s WHERE s.game_id = :gameID")
	public SaveGame getGameWithId(@Param("gameID") String gameID);
	
	@Query("SELECT s FROM SaveGame s WHERE s.username_id = :username_id")
	public Page<SaveGame> findPagedGamesWithUsernameId(@Param("pageable") Pageable pageable, 
		@Param("username_id") String username_id);
	
}

	
