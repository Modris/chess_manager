package com.modris.exceptions;

public class StatisticsNotFoundException extends RuntimeException {

	public StatisticsNotFoundException(String username_id) {
		super("Statistics for user id: `"+username_id+"` not found.");
	}
}
