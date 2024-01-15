package com.modris.model;

import org.springframework.stereotype.Component;

@Component
public class Fen {

	private String fen;
	private String userId;
	public Fen() {}
	
	public Fen(String fen, String userId) {
		this.fen = fen;
		this.userId = userId;
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
