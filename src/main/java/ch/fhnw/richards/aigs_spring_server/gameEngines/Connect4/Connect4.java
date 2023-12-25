package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

import ch.fhnw.richards.aigs_spring_server.game.Game;
import ch.fhnw.richards.aigs_spring_server.gameEngines.GameEngine;

import java.util.HashMap;

    public class Connect4 implements GameEngine {
        private boolean playerMove = true; // To alternate between player and AI

        @Override
        public Game newGame(Game game) {
            game.setBoard(new long[6][7]);
            game.setResult(false);
            return game;
        }

        @Override
        public Game move(Game game, HashMap<String, String> move) {
            int col = Integer.parseInt(move.get("col"));
            long[][] board = game.getBoard();

            // Check if the column is full
            if (board[0][col] != 0) {
                return game;
            }

            // Find the bottom-most empty row in the specified column
            int rowToPlace = -1;
            for (int row = board.length - 1; row >= 0; row--) {
                if (board[row][col] == 0) {
                    rowToPlace = row;
                    break;
                }
            }

            // Place the player's move in the found row
            if (rowToPlace != -1 && !game.getResult()) {
                board[rowToPlace][col] = playerMove ? 1 : -1;
                playerMove = !playerMove; // Switch turn
                game.setResult(getResult(board));
            }

            // If it's now AI's turn, make its move
            if (!game.getResult() && !playerMove) {
                C4_AI player = (game.getDifficulty() <= 1) ? new RandomPlayer() : new MinmaxPlayer();
                player.makeMove(board);
                playerMove = true; // Switch back to player's turn
                game.setResult(getResult(board));
                if (isDraw(board)) {
                    game.setResult(true);
                }
            }

            return game;
        }


        private boolean getResult(long[][] board) {
            return getWinner(board) != null;
        }
        static Long getWinner(long[][] board) {
            long winner = 0;
            // Check all rows for horizontal win
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[0].length - 3; col++) {
                    if (board[row][col] != 0 &&
                            board[row][col] == board[row][col + 1] &&
                            board[row][col] == board[row][col + 2] &&
                            board[row][col] == board[row][col + 3]) {
                        winner = board[row][col];
                        return winner;
                    }
                }
            }

            // Check all columns for vertical win
            for (int col = 0; col < board[0].length; col++) {
                for (int row = 0; row < board.length - 3; row++) {
                    if (board[row][col] != 0 &&
                            board[row][col] == board[row + 1][col] &&
                            board[row][col] == board[row + 2][col] &&
                            board[row][col] == board[row + 3][col]) {
                        winner = board[row][col];
                        return winner;
                    }
                }
            }

            // Check for diagonal win (down-right and up-right)
            for (int row = 0; row < board.length - 3; row++) {
                for (int col = 0; col < board[0].length - 3; col++) {
                    if (board[row][col] != 0 &&
                            board[row][col] == board[row + 1][col + 1] &&
                            board[row][col] == board[row + 2][col + 2] &&
                            board[row][col] == board[row + 3][col + 3]) {
                        winner = board[row][col];
                        return winner;
                    }
                }
            }
            for (int row = 3; row < board.length; row++) {
                for (int col = 0; col < board[0].length - 3; col++) {
                    if (board[row][col] != 0 &&
                            board[row][col] == board[row - 1][col + 1] &&
                            board[row][col] == board[row - 2][col + 2] &&
                            board[row][col] == board[row - 3][col + 3]) {
                        winner = board[row][col];
                        return winner;
                    }
                }
            }

            // No winner found
            return null;
        }
        private boolean isDraw(long[][] board) {
            // Check if any cell is empty (0)
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[0].length; col++) {
                    if (board[row][col] == 0) {
                        return false; // Not a draw since there's at least one empty cell
                    }
                }
            }
            // If all cells are filled and no winner, it's a draw
            return getWinner(board) == null;
        }

    }