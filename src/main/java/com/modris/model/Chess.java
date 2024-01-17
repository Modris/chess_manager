package com.modris.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chess_games")
public class Chess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String fen;
	
	private String move;
	
	public Chess() {};
	
	public Chess(String name, String fen, String move) {
		this.name = name;
		this.fen = fen;
		this.move = move;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}
	
}
