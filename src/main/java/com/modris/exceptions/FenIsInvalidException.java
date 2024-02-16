package com.modris.exceptions;

public class FenIsInvalidException extends RuntimeException{

	public FenIsInvalidException(String errorMessage, String fen) {
		super(errorMessage+". Fen: "+fen);
	}
}
