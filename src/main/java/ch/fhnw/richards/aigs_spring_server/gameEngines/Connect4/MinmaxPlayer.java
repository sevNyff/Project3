package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinmaxPlayer implements C4_AI {
    private long myPiece = -1; // Which player are we?

    private enum Evaluation { LOSS, DRAW, WIN }

    private class MoveEval {
        int col;
        Evaluation evaluation;

        public MoveEval(int col, Evaluation evaluation) {
            this.col = col;
            this.evaluation = evaluation;
        }
    }

    @Override
    public void makeMove(long[][] board) {
        int move = findMove(board, myPiece).col;
        int row = findFirstEmptyRow(board, move);
        if (row != -1) {
            board[row][move] = myPiece;
        }
        System.out.println("AI Move Executed.");
    }

    private MoveEval findMove(long[][] board, long toMove) {
        int bestCol = -1; // Initialize to an invalid column
        Evaluation bestEval = Evaluation.LOSS;

        // Check for a win, loss, or draw situation first
        Long result = Connect4.getWinner(board);
        if (result != null) {
            if (result == toMove) return new MoveEval(-1, Evaluation.WIN);
            else if (result == (0 - toMove)) return new MoveEval(-1, Evaluation.LOSS);
            else return new MoveEval(-1, Evaluation.DRAW);
        } else {
            // Iterate through columns to find the best move
            for (int col = 0; col < board[0].length; col++) {
                int row = findFirstEmptyRow(board, col);
                if (row != -1) {
                    // Print the evaluated column and row
                    System.out.println("Evaluating column: " + col + ", row: " + row);

                    // Simulate the move
                    long[][] tempBoard = copyBoard(board);
                    tempBoard[row][col] = toMove;
                    Evaluation tempEval = minimax(tempBoard, 0, false, (toMove == 1) ? -1 : 1);

                    // Choose the best move
                    if (tempEval.ordinal() > bestEval.ordinal()) {
                        bestEval = tempEval;
                        bestCol = col;
                    }
                }
            }

            if (bestCol == -1) {
                // No valid moves found, return any column instead of -1
                return new MoveEval(playRandomMove(board), Evaluation.LOSS);
            }

            System.out.println("Best column: " + bestCol + ", Best evaluation: " + bestEval);
            return new MoveEval(bestCol, bestEval);
        }
    }

    private int playRandomMove(long[][] board) {
        List<Integer> availableColumns = new ArrayList<>();
        for (int col = 0; col < board[0].length; col++) {
            if (board[0][col] == 0) { // Check if the top of the column is empty
                availableColumns.add(col);
            }
        }

        if (!availableColumns.isEmpty()) {
            Random rand = new Random();
            return availableColumns.get(rand.nextInt(availableColumns.size()));
        }

        return 0; // Return column 0 if no other move is available (should not happen if called correctly)
    }



    private Evaluation minimax(long[][] board, int depth, boolean isMaximizingPlayer, long currentPlayer) {
        Long result = Connect4.getWinner(board);
        if (result != null) {
            if (result == myPiece) return Evaluation.WIN;
            else if (result == (0 - myPiece)) return Evaluation.LOSS;
            else return Evaluation.DRAW;
        }

        if (depth >= 6) { // Limiting depth for performance
            return Evaluation.DRAW;
        }

        Evaluation bestEval = isMaximizingPlayer ? Evaluation.LOSS : Evaluation.WIN;

        for (int col = 0; col < board[0].length; col++) {
            int row = findFirstEmptyRow(board, col);
            if (row != -1) {
                long[][] tempBoard = copyBoard(board);
                tempBoard[row][col] = currentPlayer;
                Evaluation eval = minimax(tempBoard, depth + 1, !isMaximizingPlayer, -currentPlayer);

                // Print the evaluated column, row, and evaluation
                System.out.println("Depth: " + depth + ", Column: " + col + ", Row: " + row + ", Evaluation: " + eval);

                if (isMaximizingPlayer && eval.ordinal() > bestEval.ordinal()) {
                    bestEval = eval;
                } else if (!isMaximizingPlayer && eval.ordinal() < bestEval.ordinal()) {
                    bestEval = eval;
                }
            }
        }
        System.out.println("Best evaluation: " + bestEval);
        return bestEval;
    }

    private int findFirstEmptyRow(long[][] board, int col) {
        // Check if the column is not full
        if (board[0][col] != 0) {
            return -1; // Column is full
        }

        // Iterate through rows to find the first empty row
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return -1; // Column is full, should not happen if called correctly
    }


    private long[][] copyBoard(long[][] board) {
        long[][] newBoard = new long[board.length][];
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = new long[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }
}
