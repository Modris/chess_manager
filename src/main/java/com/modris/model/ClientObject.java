package com.modris.model;

import org.springframework.stereotype.Component;

@Component
public class ClientObject{

	private String fen;
	private String userId;
	private int chosenElo; // SimpleMessageConverter only supports String, byte[] and Serializable payloads,
	private String move;
	
	public ClientObject() {}
	
	public ClientObject(String fen, String userId, int chosenElo, String move) {
		this.fen = fen;
		this.userId = userId;
		this.chosenElo = chosenElo;
		this.move = move;
	}

	
	


	@Override
	public String toString() {
		return "ClientObject [fen=" + fen + ", userId=" + userId + ", chosenElo=" + chosenElo + ", move=" + move + "]";
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public int getChosenElo() {
		return chosenElo;
	}

	public void setChosenElo(int chosenElo) {
		this.chosenElo = chosenElo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}
	
	
}
