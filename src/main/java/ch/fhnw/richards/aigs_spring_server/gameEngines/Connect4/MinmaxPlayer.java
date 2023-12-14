package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

import ch.fhnw.richards.aigs_spring_server.gameEngines.TicTacToe.Move;

public class MinmaxPlayer implements C4_AI{
    private long myPiece = -1; // Which player are we?

    private enum Evaluation { LOSS, DRAW, WIN };

    private class MoveEval {
        Move move;
        Evaluation evaluation;

        public MoveEval(Move move, Evaluation evaluation) {
            this.move = move;
            this.evaluation = evaluation;
        }
    }

    @Override
    public void makeMove(long[][] board) {
        Move move = findMove(board, myPiece).move;
        board[move.getRow()][move.getCol()] = myPiece;
    }

    private MoveEval findMove(long[][] board, long toMove) {
        Move bestMove = null;
        Evaluation bestEval = Evaluation.LOSS;

        // TODO: Check for a winner or draw in the Connect 4 game

        // Find the best move using the minimax algorithm
        for (int col = 0; col < board[0].length; col++) {
            if (isValidMove(board, col)) {
                // Possible move found
                long[][] possBoard = copyBoard(board);
                int row = dropPiece(possBoard, col, toMove);
                MoveEval tempMoveEval = findMove(possBoard, -toMove);
                Evaluation possEval = invertResult(tempMoveEval.evaluation);

                if (possEval.ordinal() > bestEval.ordinal()) {
                    bestEval = possEval;
                    bestMove = new Move(toMove, col, row);
                }
            }
        }

        return new MoveEval(bestMove, bestEval);
    }

    private int dropPiece(long[][] board, int col, long player) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = player;
                return row;
            }
        }
        return -1; // Column is full
    }

    private boolean isValidMove(long[][] board, int col) {
        return board[0][col] == 0; // Check if the top row of the column is empty
    }

    private Evaluation invertResult(Evaluation in) {
        if (in == Evaluation.DRAW) return Evaluation.DRAW;
        else if (in == Evaluation.WIN) return Evaluation.LOSS;
        return Evaluation.WIN;
    }

    private long[][] copyBoard(long[][] board) {
        long[][] newboard = new long[board.length][];
        for (int i = 0; i < board.length; i++) {
            newboard[i] = new long[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                newboard[i][j] = board[i][j];
            }
        }
        return newboard;
    }
}
