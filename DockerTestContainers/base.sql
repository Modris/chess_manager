CREATE DATABASE IF NOT EXISTS testdb;
use testdb;

	CREATE TABLE chess_games (
    id INT AUTO_INCREMENT PRIMARY KEY,
    moves TEXT,
    fen TEXT, 
    winner VARCHAR(40),
	color VARCHAR(5),
	elo VARCHAR(4), 
	username_id VARCHAR(255),
    game_id VARCHAR(68),
    wins  SMALLINT UNSIGNED,
    losses SMALLINT UNSIGNED,
    draws SMALLINT UNSIGNED
);

INSERT INTO chess_games (moves, fen, winner, color, elo, username_id, game_id, wins, losses, draws)
VALUES
('d2d4,b7b6,e2e4,c8b7,d4d5', 'rn1qkbnr/pbpppppp/1p6/8/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 1 3', 'Black resigns.', 'black', '1770', 'da903091-e26b-458c-84ef-fd1945359f35', 'b59ce5bd025e4e28acf2ba7ee1d41d74', '0', '1', '0'),
('d2d3,d7d5,e1d2,c7c5,d2c3,d8a5,c3b3,a5b6,b3a4,b6a5,a4b3,a5b5,b3a3,b5b4', 'rnb1kbnr/pp2pppp/8/1qpp4/8/K2P4/PPP1PPPP/RNBQ1BNR b kq - 9 7', 'Black is victorious', 'white', '2834', '1a309832-7e10-47e8-8d56-1cf01da1198b', '155131bf169e43e59898bcf098ef8795', '0', '1', '0'),
('e2e4,b7b6,d2d4,c8b7,f1d3,d7d6,g1f3,b8d7,c2c4,g7g6,h2h3,f8g7,b1c3,g8f6,c1e3,e8g8,d3c2,e7e6,a2a4,c7c5,d4d5,e6d5,e4d5,d7e5,f3e5,d6e5,g2g4,f6d7,c3e4,d8c7,d1e2,d7f6,e4f6,g7f6,e1c1,f8e8,g4g5,f6g7,c2e4,a7a6,h3h4,b6b5,h4h5,b5c4,e2c4,a6a5,c1b1,a8c8,d1c1,g7f8,f2f3,c7d6,e3d2,b7a6,c4c2,a6e2,h1h2,e2a6,c1h1,c5c4,h5g6,f7g6,h2h7,f8g7,d2c3,c8c7,h1h6,a6c8,c3a5,c7a7,h6g6,d6e7,c2h2,a7a5,h2h5,g8f8,h7h8,g7h8,h5h8,f8f7,h8h7,f7f8,h7h8,f8f7,g6g7', '2b1r2Q/4qk2/6R1/r2Pp1P1/P1p1B3/5P2/1P6/1K6 w - - 5 43', 'White is victorious', 'black', '2389', 'da903091-e26b-458c-84ef-fd1945359f35', '10416eb106974d75aaa3b72cc1402332', '0', '1', '0');
