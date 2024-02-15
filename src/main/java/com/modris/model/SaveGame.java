package com.modris.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "chess_games")
public class SaveGame {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String winner;
	
	private String elo;
	
	private String color;
	
	private String moves;
	
	private String fen;
	
	private String username_id;
	
	private String game_id;
	
	@Transient
	private String result; 
	
	private int wins;
	private int losses;
	private int draws;
	
	public SaveGame() {}
	
	public SaveGame(String winner, String elo, String color, String moves, String fen, String result) {
		this.winner = winner;
		this.elo = elo;
		this.color = color;
		this.moves = moves;
		this.fen = fen;
		this.result = result;
	}

	
	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	
	public String getUsername_id() {
		return username_id;
	}

	public void setUsername_id(String username_id) {
		this.username_id = username_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getElo() {
		return elo;
	}

	public void setElo(String elo) {
		this.elo = elo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMoves() {
		return moves;
	}

	public void setMoves(String moves) {
		this.moves = moves;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	@Override
	public String toString() {
		return "SaveGame [id=" + id + ", winner=" + winner + ", elo=" + elo + ", color=" + color + ", moves=" + moves
				+ ", fen=" + fen + "]";
	}
	
	
	
}
