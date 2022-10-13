package ch.fhnw.richards.aigs_spring_server.gameEngines.TicTacToe;

import java.util.HashMap;
import java.util.Optional;

import ch.fhnw.richards.aigs_spring_server.game.Game;
import ch.fhnw.richards.aigs_spring_server.game.GameController;
import ch.fhnw.richards.aigs_spring_server.game.GameRepository;
import ch.fhnw.richards.aigs_spring_server.gameEngines.GameEngine;

/**
 * Game board encoding: -1 = O, 0 = empty, 1 = X
 * 
 * Game difficulty: 1 or less = random-player, 2 = decent player, 3 or more = optimal player
 */
public class TicTacToe implements GameEngine {

	
	@Override
	public Game newGame(Game game) {
		GameRepository rep = GameController.getRepository();
		long[][] board = new long[3][3];
		game.setBoard(board);
		game.setResult(null); // status playing
		Boolean playerFirst = game.getPlayerFirst();
		//if (playerFirst == false) TTT_random(board);
		return rep.save(game);
	}

	@Override
	public Game move(Game game, HashMap<String, String> move) {
		// TODO Auto-generated method stub
		return game;
	}

}
