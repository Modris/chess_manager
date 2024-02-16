package com.modris.validation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.modris.model.SaveGame;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest(classes = SaveGame.class)
public class SaveGameValidationTests {
	


	private static Validator validator;
	   
	@BeforeAll
	static void setUp() {
	        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	        validator = factory.getValidator();

	 }
/*
	public SaveGame(String winner, String elo, 
			String color, String moves, String fen, String result) {
*/

	@Test
	@DisplayName("Validation passes. Everything is correct.")
	void whenAllFieldsCorrect() {
		var game = new SaveGame("Black resigns.","3190","white","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		var violations = validator.validate(game);
		assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
		
	}
	
	@Test
	@DisplayName("Elo is below minimum (1320). Validation fails test.")
	void eloBelowMinimumFail() {
		var game = new SaveGame("Black resigns.","1319","white","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(properties.get(0).equals("elo"));
		 assertTrue(messages.size() == 1, () -> "Only Elo should fail. Other validations should pass.");
		 assertEquals("Elo must be at least 1320", messages.get(0), () -> "Only Elo should fail and it should be the 1st message.");
		
	}
	
	@Test
	@DisplayName("Elo is above maximum (3190). Validation fails test.")
	void eloAboveMaximumFail() {
		var game = new SaveGame("Black resigns.","3191","white","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(properties.get(0).equals("elo"));
		 assertTrue(messages.size() == 1, () -> "Only Elo should fail. Other validations should pass.");
		 assertEquals("Elo must be at most 3190", messages.get(0), () -> "Only Elo should fail and it should be the 1st message.");
		
	}
	
	@Test
	@DisplayName("Color should be white or black. Validation fails test.")
	void colorFailsTest() {
		var game = new SaveGame("Black resigns.","3190","whit2e","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(properties.get(0).equals("color"));
		 assertTrue(messages.size() == 1, () -> "Only Color should fail. Other validations should pass.");
		 assertEquals("must match \"black|white\"", messages.get(0), () -> "Only Color should fail and it should be the 1st message.");
	}
	
	@Test
	@DisplayName("Color is black. Correct.")
	void colorCorrectTest() {
		var game = new SaveGame("Black resigns.","3190","black","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		var violations = validator.validate(game);
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	@Test
	@DisplayName("Color is white. Correct.")
	void colorCorrectTest2() {
		var game = new SaveGame("Black resigns.","3190","white","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		var violations = validator.validate(game);
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	@Test
	@DisplayName("Correct Move List (1). Regular pieces with comma.")
	void correctMoveList1() {
		var game = new SaveGame("Black resigns.","3190","white","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		game.setMoves("d2d3,d7d5,e1d2,c7c5,d2c3,d8a5,c3b3,a5b6,b3a4,b6a5,a4b3,a5b5,b3a3,b5b4");
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (2). This one contains advanced piece (h2h1n).")
	void correctMoveList2() {
		var game = new SaveGame("Black resigns.","3190","white","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		game.setMoves("g1f3,d7d5,d2d4,c7c6,g2g3,g8f6,f1g2,c8f5,e1g1,h7h5,e2e3,e7e6,b2b3,f8e7,c1b2,b8d7,b1d2,f6e4,c2c4,e4d6,a1c1,h5h4,a2a3,a7a5,b3b4,h4h3,g2h1,d6c4,d2c4,d5c4,c1c4,d7b6,c4c1,a5b4,a3b4,a8a2,b2c3,f5e4,d1b3,d8d5,b3d5,b6d5,f3e5,e4h1,g1h1,e8g8,h1g1,f8a8,e5d7,d5e3,f2e3,a2g2,g1h1,a8a2,f1d1,g2h2,h1g1,h2g2,g1f1,a2f2,f1e1,h3h2,d1d2,f2d2,c3d2,h2h1n,e1d1,h1g3,d2c3,g3f1,c1b1,f1e3,d1c1,g2c2");
		
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (3). Many moves. 199. ")
	void correctMoveList3() {
		var game = new SaveGame("Black resigns.","3190","white","a2a4,b3b2"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
		game.setMoves("g1f3,b7b6,b1c3,c8b7,g2g3,d7d6,e2e4,b8d7,f1g2,g7g6,d2d4,f8h6,d4d5,h6c1,d1c1,g8f6,e1g1,f6g4,c1g5,g4e5,f3e1,c7c6,e1d3,c6d5,g5h6,d7f6,d3e5,d6e5,e4d5,e8d7,h6g5,d7e8,f1e1,e8f8,g5h6,f8g8,h2h3,d8f8,h6e3,g8g7,e3e5,f8d8,e5e7,d8e7,e1e7,b7a6,a1d1,h8e8,e7e3,e8e3,f2e3,a8e8,e3e4,f6h5,d1d4,h5g3,d5d6,g3e2,c3e2,a6e2,g1f2,e2b5,c2c4,b5d7,h3h4,g7f6,f2e3,f6e5,d4d5,e5e6,b2b4,d7c6,d5d2,e8d8,b4b5,c6b7,g2h3,e6e5,c4c5,b6c5,h3g2,d8d6,d2c2,d6b6,c2c5,e5e6,c5c7,b6b5,g2f1,b5b6,h4h5,a7a5,e3d4,b7c6,f1c4,e6d6,c7a7,b6b4,e4e5");
		
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (4). Basic h2h3 test. Valid. ")
	void correctMoveList4() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (5). Piece advancement for Knight Valid. ")
	void correctMoveList5() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,a7a8n"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (6). Piece advancement for Queen Valid. ")
	void correctMoveList6() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,a7a8q"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	@Test
	@DisplayName("Correct Move List (7). Piece advancement for Bishop Valid. ")
	void correctMoveList7() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,a7a8b"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (8). Piece advancement for Rook Valid. ")
	void correctMoveList8() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,a7a8r"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (9). Basic piece with comma Valid. ")
	void correctMoveList9() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }

		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Correct Move List (10). Advanced piece with comma Valid. ")
	void correctMoveList10() {
		var game = new SaveGame("Black resigns.","3190","white","a7a8n,"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }

		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Invalid move (1). No spaces allowed at the start.")
	void invalidMove1() {
		var game = new SaveGame("Black resigns.","3190","white"," a7a8,"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(properties.get(0).equals("moves"));
		 assertTrue(messages.size() == 1, () -> "Only Move should fail. Other validations should pass.");
		 assertEquals("must match \"([abcdefgh][1-8][abcdefgh][1-8][qnbr]?,?)+\"",messages.get(0), () -> "Only Move should fail. Other validations should pass.");
	}
	
	@Test
	@DisplayName("Invalid move (2). No spaces allowed at the end")
	void invalidMove2() {
		var game = new SaveGame("Black resigns.","3190","white","a7a8, "
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(properties.get(0).equals("moves"));
		 assertTrue(messages.size() == 1, () -> "Only Move should fail. Other validations should pass.");
		 assertEquals("must match \"([abcdefgh][1-8][abcdefgh][1-8][qnbr]?,?)+\"",messages.get(0), () -> "Only Move should fail. Other validations should pass.");
	}
	
	@Test
	@DisplayName("Invalid move (3). No spaces allowed between moves.")
	void invalidMove3() {
		var game = new SaveGame("Black resigns.","3190","white","a7a8, b7b8"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }
		 assertTrue(properties.get(0).equals("moves"));
		 assertTrue(messages.size() == 1, () -> "Only Move should fail. Other validations should pass.");
		 assertEquals("must match \"([abcdefgh][1-8][abcdefgh][1-8][qnbr]?,?)+\"",messages.get(0), () -> "Only Move should fail. Other validations should pass.");
	}
	
	@Test
	@DisplayName("Result Correct. Draw.")
	public void resultCorrect() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Draw");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }

		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Result Correct (2). Loss.")
	public void resultCorrect2() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Loss");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }

		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Result Correct (3). Win.")
	public void resultCorrect3() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }

		 assertTrue(violations.isEmpty(), () -> "Validation should pass and the list should be empty.");
	}
	
	@Test
	@DisplayName("Result fails. Wins is not Win|Draw|Loss.")
	public void resultIncorrect() {
		var game = new SaveGame("Black resigns.","3190","white","h2h3,"
				,"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1","Win2s");
	
		var violations = validator.validate(game);
		List<String> properties = new ArrayList<>();
		List<String> messages = new ArrayList<>();
		 for (ConstraintViolation<SaveGame> violation : violations) {
			 properties.add(violation.getPropertyPath().toString());
			 messages.add(violation.getMessage());
		    }

		 assertTrue(properties.get(0).equals("result"));
		 assertTrue(messages.size() == 1, () -> "Only Result should fail. Other validations should pass.");
		 assertEquals("must match \"Draw|Win|Loss\"",messages.get(0), () -> "Only Result should fail. Other validations should pass.");	 
	}
	
}
