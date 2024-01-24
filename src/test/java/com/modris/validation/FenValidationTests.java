package com.modris.validation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.modris.model.FenValidator;

@SpringBootTest(classes = FenValidator.class)
public class FenValidationTests {
/*
 Only start FenValidator class for Unit tests. This disables RabbitMQ connection, 
 Spring data JPA connection to the database and speeds up the Unit test.
 */
	
	@Autowired
	private FenValidator fenValidator;
// Eight Parts Check START~~~~~~~~~~~~~
	@Test
	@DisplayName("Fen invalid 8 parts check I. 9 parts.")
	public void EightParts1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/test w KQkqa - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The fen should be divided into eight parts by the Slash / symbol.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid 8 parts check II. 7 parts.")
	public void EightParts2() {
		boolean answer = fenValidator.isFenValid("pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkqa - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The fen should be divided into eight parts by the Slash / symbol.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen VALID 8 parts check I.")
	public void EightPartsValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
		assertTrue(answer);

	}
	
// Eight Parts Check END~~~~~~~~~~~~~~	

// 	Six parts check START~~~~~~~~~~~~~
	
	@Test
	@DisplayName("Fen VALID 6 parts check I.")
	public void SixPartsValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
		assertTrue(answer);

	}
	
	@Test
	@DisplayName("Fen invalid 6 parts check I. 5 parts.")
	public void SixParts1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The fen for piece distribution should be divided into 6 parts.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid 6 parts check I. 7 parts.")
	public void SixParts2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The fen for piece distribution should be divided into 6 parts.",
				fenValidator.getErrorMessage());
	}
// Six parts Check END~~~~~~~~~~~~~	
	
	@Test
	@DisplayName("Fen VALID 2 kings check I.")
	public void TwoKingsValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen invalid 2 kings check I. kk check but there can only be kK.")
	public void TwoKings1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQkBNR w KQkq - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Each side should have only 2 kings. No more, no less.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid 2 kings check II. 1 king.")
	public void TwoKings2() {
		boolean answer = fenValidator.isFenValid("rnbqpbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQkBNR w KQkq - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Each side should have only 2 kings. No more, no less.",
				fenValidator.getErrorMessage());
	}
// Two King Check END~~~~~~~~~~~~~
	
// Half Move Check START~~~~~~~~~~~~~
	@Test
	@DisplayName("Fen invalid Half move I. Not positive integer. Below 0.")
	public void HalfMove1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - -1 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The half move counter should be positive integer above or equal to 0."
				,fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid Half move II. Not positive integer.")
	public void HalfMove2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0.1 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The half move counter should be positive integer above or equal to 0."
				,fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid Half move III. Non integer test.")
	public void HalfMove3() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - abcd 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The half move counter should be positive integer above or equal to 0."
				,fenValidator.getErrorMessage());
	}

	@Test
	@DisplayName("Fen VALID Half move I. Positive integer given.")
	public void HalfMoveValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 2 1");
		assertTrue(answer);
	}

// Half Move Check END~~~~~~~~~~~~~
	
// Full Move Check Start~~~~~~~~~~~~~
	
	@Test
	@DisplayName("Fen invalid Full move I. 0 given.")
	public void FullMove1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The full move counter should be positive integer above or equal to 1."
				,fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid Full move II. Not positive integer.")
	public void FullMove2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1.1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The full move counter should be positive integer above or equal to 1."
				,fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid Full move III. Non integer test.")
	public void FullMove3() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 abcd");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The full move counter should be positive integer above or equal to 1."
				,fenValidator.getErrorMessage());
	}

	@Test
	@DisplayName("Fen VALID Full move I. Positive integer given.")
	public void FullMoveValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 2 3");
		assertTrue(answer);
	}
	
// Full Move Check End~~~~~~~~~~~~~
	
// En Pessant Check START~~~~~~~~~~~~~~

	@Test
	@DisplayName("Fen VALID en pessant check. - given")
	public void enPessantValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 2 3");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen VALID en pessant check. Valid Square a8 given.")
	public void enPessantValid2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq a8 2 3");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen invalid en pessant check I. Exceeds max length.")
	public void enPessant1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq a8a 2 3");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. En passant location in the fen (after king) either '-' or valid square like 'e6' is possible. Max length 1 or 2.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid en pessant check II. Invalid square a-.")
	public void enPessant2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq a- 2 3");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. En passant location in the fen (after king) either '-' or valid square like 'e6' is possible. Max length 1 or 2.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid en pessant check III. Invalid square --.")
	public void enPessant3() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -- 2 3");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. En passant location in the fen (after king) either '-' or valid square like 'e6' is possible. Max length 1 or 2.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid en pessant check IV. Invalid square 8h.")
	public void enPessant4() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq 8h 2 3");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. En passant location in the fen (after king) either '-' or valid square like 'e6' is possible. Max length 1 or 2.",
				fenValidator.getErrorMessage());
	}
// En Pessant Check END~~~~~~~~~~~~~~~~
	
// Sixteen piece check START~~~~~~~~~~~~
	
	@Test
	@DisplayName("Fen VALID. Sixteen piece check. 32 pieces in total. 16 pieces each.")
	public void sixteenPieceCheckValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen VALID. Sixteen piece check. Valid piece syntax.")
	public void sixteenPieceCheckValid2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertTrue(answer);
	}
	
	
	@Test
	@DisplayName("Fen Invalid. Sixteen piece check. 17 pieces for white. 33 pieces in total.")
	public void sixteenPieceCheck1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/7P/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen Invalid. Sixteen piece check. 17 pieces for Black. 33 pieces in total.")
	public void sixteenPieceCheck2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/7p/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen Invalid. Sixteen piece check. Each row should have sum of eight.")
	public void sixteenPieceCheck3() {
		boolean answer = fenValidator.isFenValid("3k5/8/8/8/8/8/8/2K5 w - - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen Invalid. Sixteen piece check. Each row should have sum of eight.")
	public void sixteenPieceCheck4() {
		boolean answer = fenValidator.isFenValid("2k5/9/8/8/8/8/8/2K5 w - - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.",
				fenValidator.getErrorMessage());
	}
	@Test
	@DisplayName("Fen Invalid. Sixteen piece check. Each row should have sum of eight.")
	public void sixteenPieceCheck5() {
		boolean answer = fenValidator.isFenValid("2k5/8/7/8/8/8/8/2K5 w - - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen Invalid. Sixteen piece check. Invalid piece syntax")
	public void sixteenPieceCheck6() {
		boolean answer = fenValidator.isFenValid("r1bqkb1r/pppn1ppp/3p1n2/4p3/3PaP2/2N5/PPP3PP/R1BQKBNR w KQkq - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. Check if it's valid piece syntax, sum of 8 for all pieces or one side exceeds 16 piece rule limit.",
				fenValidator.getErrorMessage());
	}
	
	
// Sixteen piece check END~~~~~~~~~~~~~~
	
// Active Color check START~~~~~~~~~~~~~	
	@Test
	@DisplayName("Fen invalid Active Color check I. Not w or b letter.")
	public void ActiveColor1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR c KQkq - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The active color move is either w or b. Nothing else.",
				fenValidator.getErrorMessage());
	}
	@Test
	@DisplayName("Fen invalid Active Color check II. Max length can't exceed 1.")
	public void ActiveColor2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR wb KQkq - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The active color move is either w or b. Nothing else.",
				fenValidator.getErrorMessage());
	}
	@Test
	@DisplayName("Fen VALID. Active Color check I. Color choice: w")
	public void ActiveColorValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen VALID. Active Color check II. Color choice: b")
	public void ActiveColorValid2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
		assertTrue(answer);
	}
// Active Color Check END~~~~~~~~~~~~~
	
// CASTLING CHECK START~~~~~~~~~~~~~
	@Test
	@DisplayName("Fen invalid Castling check I. 5 characters. Max is 4 allowed.")
	public void castling1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkqa - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The castling can either have KQ, kq or - symbol.",
				fenValidator.getErrorMessage());
	}
	@Test
	@DisplayName("Fen invalid Castling check II. Invalid character other than k,q,-")
	public void castling2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQka - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The castling can either have KQ, kq or - symbol.",
				fenValidator.getErrorMessage());
	}
	
	@Test
	@DisplayName("Fen invalid Castling check III. Character - can't exist if it's not alone.")
	public void castling3() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk- - 0 1");
		assertFalse(answer);
		assertEquals("Fen validation syntax is invalid. The castling can either have KQ, kq or - symbol.",
				fenValidator.getErrorMessage());
	}

	
	@Test
	@DisplayName("Fen VALID Castling check I. - character solo check")
	public void castlingValid1() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen VALID Castling check II. KQ test.")
	public void castlingValid2() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen VALID Castling check III. k test.")
	public void castlingValid3() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w k - 0 1");
		assertTrue(answer);
	}
	
	@Test
	@DisplayName("Fen VALID Castling check IV. q test.")
	public void castlingValid4() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w q - 0 1");
		assertTrue(answer);
	}
	@Test
	@DisplayName("Fen VALID Castling check V. Qkq test.")
	public void castlingValid5() {
		boolean answer = fenValidator.isFenValid("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Qkq - 0 1");
		assertTrue(answer);
	}
	
// CASTLING CHECK END~~~~~~~~~~~~~
	
	
}
