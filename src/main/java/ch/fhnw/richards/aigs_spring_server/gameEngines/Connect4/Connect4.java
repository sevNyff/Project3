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

/*
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
        */
    }