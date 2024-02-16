package com.modris.exceptions;

public class GameNotFoundException extends RuntimeException {

	public GameNotFoundException(String gameID) {
		super("A book with game id: `"+gameID+"` not found.");
	}
}
