package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

import ch.fhnw.richards.aigs_spring_server.game.Game;
import ch.fhnw.richards.aigs_spring_server.gameEngines.GameEngine;

import java.util.HashMap;

public class Connect4 implements GameEngine {

    @Override
    public Game newGame(Game game) {
        game.setBoard(new long[6][7]);
        game.setResult(false);
        return game;
    }

    @Override
    public Game move(Game game, HashMap<String, String> move) {
        int row = Integer.parseInt(move.get("row"));
        int col = Integer.parseInt(move.get("col"));

        // Only accept the player's move if it is valid
        if (game.getBoard()[row][col] == 0) {
            game.getBoard()[row][col] = 1;
            game.setResult(getResult(game.getBoard()));

            if (!game.getResult()) {
                // Make our move.
                C4_AI player = (game.getDifficulty() <= 1) ? new RandomPlayer() : new MinmaxPlayer();
                player.makeMove(game.getBoard());
            }
            game.setResult(getResult(game.getBoard()));
        }
        return game;
    }

    private boolean getResult(long[][] board) {
        return getWinner(board) != null;
    }

    static Long getWinner(long[][] board) {
        // Check for a win horizontally
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col <= board[row].length - 4; col++) {
                if (checkConsecutive(board[row][col], board[row][col + 1], board[row][col + 2], board[row][col + 3])) {
                    return board[row][col];
                }
            }
        }

        // Check for a win vertically
        for (int col = 0; col < board[0].length; col++) {
            for (int row = 0; row <= board.length - 4; row++) {
                if (checkConsecutive(board[row][col], board[row + 1][col], board[row + 2][col], board[row + 3][col])) {
                    return board[row][col];
                }
            }
        }

        // Check for a win diagonally (left to right)
        for (int row = 0; row <= board.length - 4; row++) {
            for (int col = 0; col <= board[row].length - 4; col++) {
                if (checkConsecutive(board[row][col], board[row + 1][col + 1], board[row + 2][col + 2], board[row + 3][col + 3])) {
                    return board[row][col];
                }
            }
        }

        // Check for a win diagonally (right to left)
        for (int row = 0; row <= board.length - 4; row++) {
            for (int col = 3; col < board[row].length; col++) {
                if (checkConsecutive(board[row][col], board[row + 1][col - 1], board[row + 2][col - 2], board[row + 3][col - 3])) {
                    return board[row][col];
                }
            }
        }

        // No winner yet
        return null;
    }

    //The checkConsecutive method is a helper function that verifies if four values are the same and not equal to zero, indicating a win.
    private static boolean checkConsecutive(long a, long b, long c, long d) {
        return a != 0 && a == b && a == c && a == d;
    }
}
