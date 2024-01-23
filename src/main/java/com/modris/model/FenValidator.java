package com.modris.model;

import org.springframework.stereotype.Component;

@Component
public class FenValidator {

	public FenValidator() {
	}

	public boolean isFenValid(String fen) {
		fen = fen.trim();
		String[] fenArray = fen.split("/"); // for Piece placement
		String[] fenArrayExtra = fen.split(" "); // for active color, castling, half move, fullmove
		String[] fenArrayPieces = fenArrayExtra[0].split("/");

		return  eightPartsCheck(fenArray) 
				&& sixPartsCheck(fenArrayExtra) 
				&& activeColor(fenArrayExtra) 
				&& halfMoveCheck(fenArrayExtra)
				&& fullMoveCheck(fenArrayExtra) 
				&& castlingCheck(fenArrayExtra) 
				&& enPassantCheck(fenArrayExtra)
				&& twoKingsCheck(fenArrayPieces) 
				&& sixteenPieceCheck(fenArrayPieces);
	}

	public boolean eightPartsCheck(String fen[]) {
		if (fen.length == 8) {
			return true;
		} else {
			throw new FenValidationEightPartException("t");
		}

	}

	public boolean sixPartsCheck(String fen[]) {
		return fen.length == 6;

	}

	public boolean activeColor(String fen[]) {
		return fen[1].length() == 1 && (fen[1].equals("w") || fen[1].equals("b"));
	}

	public boolean twoKingsCheck(String fen[]) {
		int twoKingCount = 0;
		for (int i = 0; i < fen.length; i++) {
			if (fen[i].contains("k") || fen[i].contains("K")) {
				twoKingCount++;
			}
		}
		return twoKingCount == 2;
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
		return whitePieceCount <= 16 || blackPieceCount <= 16 || (whitePieceCount + blackPieceCount) <= 32;
	}

	public boolean pieceSyntax(char piece) {
		return (piece == 'p' || piece == 'n' || piece == 'b' || piece == 'q' || piece == 'r' || piece == 'k');
	}

	public boolean pieceSyntaxNumbers(char piece) {
		return (piece == '1' || piece == '2' || piece == '3' || piece == '4' || piece == '5' || piece == '6'
				|| piece == '7' || piece == '8');
	}

	public boolean enPassantCheck(String fen[]) {
		// Either '-' or 'e6' is possible. Max length 1 or 2.
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
		if (fen[2].length() <= 4) { // max we can have kQkQ. No more.
			for (int i = 0; i < fen[2].length(); i++) {
				if (fen[2].charAt(i) == 'k' || fen[2].charAt(i) == 'q' || fen[2].charAt(i) == '-') {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
		// Future castling check: 
		// if rooks are not in their starting positions no castling rights
		
		return false;
	}

	public boolean halfMoveCheck(String fen[]) {
		return Integer.valueOf(fen[4]) >= 0;

	}

	public boolean fullMoveCheck(String fen[]) {
		return Integer.valueOf(fen[5]) >= 1;
	}
}
