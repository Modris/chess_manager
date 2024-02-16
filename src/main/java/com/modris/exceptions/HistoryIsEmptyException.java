package com.modris.exceptions;

public class HistoryIsEmptyException extends RuntimeException{

	public HistoryIsEmptyException(String username_id) {
		super("History for user id `"+username_id+"` is empty.");
	}
}
