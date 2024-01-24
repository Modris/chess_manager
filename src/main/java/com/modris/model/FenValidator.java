package com.modris.model;

import org.springframework.stereotype.Component;

@Component
public class FenValidator {
	private String errorMessage;
	
	public FenValidator() {
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isFenValid(String fen) {
		fen = fen.trim();
		String[] fenArray = fen.split("/"); // for Piece placement
		String[] fenArrayExtra = fen.split(" "); // for active color, castling, half move, fullmove
		String[] fenArrayPieces = fenArrayExtra[0].split("/");
		
		
		if(!eightPartsCheck(fenArray)) {
			setErrorMessage("Fen validation syntax is invalid. The fen should be divided into eight parts by the Slash / symbol.");
			return false;
		}
		if(!sixPartsCheck(fenArrayExtra) ) {
			setErrorMessage("Fen validation syntax is invalid. The fen for piece distribution should be divided into 6 parts.");
			return false;
		}
		if(!activeColor(fenArrayExtra) ) {
			setErrorMessage("Fen validation syntax is invalid. The active color move is either w or b. Nothing else.");
			return false;
		}
		if(!halfMoveCheck(fenArrayExtra)) {
			setErrorMessage("Fen validation syntax is invalid. The half move counter should be positive integer above or equal to 0.");
			return false;
		}
		if(!fullMoveCheck(fenArrayExtra) ) {
			setErrorMessage("Fen validation syntax is invalid. The full move counter should be positive integer above or equal to 1.");
			return false;
		}
		if(!castlingCheck(fenArrayExtra) ) {
			setErrorMessage("Fen validation syntax is invalid. The castling can either have KQ, kq or - symbol.");
			return false;
		}
		if(! enPassantCheck(fenArrayExtra)) {
			setErrorMessage("Fen validation syntax is invalid. En passant location in the fen (after king) either '-' or valid square like 'e6' is possible. Max length 1 or 2.");
			return false;
		}
		if(!twoKingsCheck(fenArrayPieces)) {
			setErrorMessage("Fen validation syntax is invalid. Each side should have only 2 kings. No more, no less.");
			return false;
		}
		if(!sixteenPieceCheck(fenArrayPieces)) {
			setErrorMessage("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.");
			return false;
		}
		return  true;
	}

	public boolean eightPartsCheck(String fen[]) {
		return fen.length == 8;

	}

	public boolean sixPartsCheck(String fen[]) {
		return fen.length == 6;
		/*
		 Six parts  1) piece order 2) active color 3) castling rights
		 4) en pessant 5) half move 6) full move
		 Example:
		 rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
			w
			-
			-
			0
			1 
		 */

	}

	public boolean activeColor(String fen[]) {
		return fen[1].length() == 1 && (fen[1].equals("w") || fen[1].equals("b"));
	}

	public boolean twoKingsCheck(String fen[]) {
		int smallKingCount = 0; // k king counter
		int bigKingCount = 0; // K king counter
		for (int i = 0; i < fen.length; i++) {
			if(fen[i].contains("k")) {
				smallKingCount++;
			}
			if (fen[i].contains("K")) {
				bigKingCount++;
			}
		}
		return smallKingCount == 1 && bigKingCount == 1;
	}

	public boolean sixteenPieceCheck(String fen[]) {
		int whitePieceCount = 0;
		int blackPieceCount = 0;
		for (int i = 0; i < fen.length; i++) {

			// Pieces should sum to eight. Otherwise return false.
			int sumOfEight = 0;
			for (int j = 0; j < fen[i].length(); j++) {
				// If characters are not valid then we return false.
				// valid characters: p,n,b,q,r,k, 1-8 numbers
				if (!pieceSyntaxNumbers(fen[i].charAt(j)) && !pieceSyntax(fen[i].toLowerCase().charAt(j))) {
					return false;
				}
				// count sum. If it's not 8 then return false.
				if (pieceSyntax(fen[i].toLowerCase().charAt(j))) {
					sumOfEight++;
				} else if (pieceSyntaxNumbers(fen[i].charAt(j))) {
					sumOfEight = sumOfEight + Character.getNumericValue(fen[i].charAt(j));
				}
				if (j == fen[i].length() - 1 && sumOfEight != 8) {
					return false;
				}

				// White and Black piece count. Max 16 for each side, 32 in total.
				if (pieceSyntax(fen[i].charAt(j)) && fen[i].charAt(j) == Character.toLowerCase(fen[i].charAt(j))) {
					blackPieceCount++;
				} else if (!pieceSyntaxNumbers(fen[i].charAt(j))) {
					whitePieceCount++;
				}
			}
		}
		return whitePieceCount <= 16 && blackPieceCount <= 16 && (whitePieceCount + blackPieceCount) <= 32;
	}

	public boolean pieceSyntax(char piece) {
		return (piece == 'p' || piece == 'n' || piece == 'b' || piece == 'q' || piece == 'r' || piece == 'k');
	}

	public boolean pieceSyntaxNumbers(char piece) {
		return (piece == '1' || piece == '2' || piece == '3' || piece == '4' || piece == '5' || piece == '6'
				|| piece == '7' || piece == '8');
	}

	public boolean enPassantCheck(String fen[]) {
		// Either '-' or valid square e.g. 'e6' is possible. Max length 1 or 2.
		if (fen[3].length() == 1 && fen[3].charAt(0) == '-') {
			return true;
		} else if (fen[3].length() == 2) {
			char file = fen[3].charAt(0);
			char rank = fen[3].charAt(1);
			if ((rank >= '1' && rank <= '8') && fileCheck(file)) {
				return true;
			}
		}
		return false;
	}

	public boolean fileCheck(char file) {
		return file == 'a' || file == 'b' || file == 'c' || file == 'd' || file == 'e' || file == 'f' || file == 'g'
				|| file == 'h';
	}

	public boolean castlingCheck(String fen[]) {
		fen[2] = fen[2].toLowerCase();
		int n = 0;
		if(fen[2].contains("-")) {
			if(fen[2].length() == 1) {
				return true; // if it contains "-" character then it can only be length 1
			} else {
				return false;
			}
		}
		if (fen[2].length() <= 4) { // max we can have kQkQ. No more.
			for (int i = 0; i < fen[2].length(); i++) {
				if (fen[2].charAt(i) == 'k' || fen[2].charAt(i) == 'q') {
					n++;

				} else {
					return false;
				}
			} // for cycle end
		} 
		if(n==fen[2].length()) {
			return true;
		}
		// Future castling check: 
		// if rooks are not in their starting positions no castling rights
		
		return false;
	}

	/*
	 HalfMove and FullMove check doesn't check the actual move count.
	 For example if halfmove is 0 then fullmove is 1. And so on.
	 Because the stockfish engine doesn't care about that when calculating
	 the best move.
	 */
	public boolean halfMoveCheck(String fen[]) {
		try {
			return Integer.valueOf(fen[4]) >= 0;
	    } catch (NumberFormatException nfe) { // not integer if thrown
	        return false;
	    }

	}

	public boolean fullMoveCheck(String fen[]) {
		try {
			return Integer.valueOf(fen[5]) >= 1;
	    } catch (NumberFormatException nfe) { // not integer if thrown
	        return false;
	    }
		
	}
	
}
