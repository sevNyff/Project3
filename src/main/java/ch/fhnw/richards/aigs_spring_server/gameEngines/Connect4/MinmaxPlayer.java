package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

import ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4.Move;

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
        // Place the move at the bottom of the column
        if (move != null) {
            int row = findFirstEmptyRow(board, move.getCol());
            if (row != -1) {
                board[row][move.getCol()] = myPiece;
            }
        }
    }

    private MoveEval findMove(long[][] board, long toMove) {
        Move bestMove = null;
        Evaluation bestEval = Evaluation.LOSS;

        // Check for a win, loss, or draw situation first
        Long result = Connect4.getWinner(board);
        if (result != null) {
            if (result == toMove) return new MoveEval(null, Evaluation.WIN);
            else if (result == (0 - toMove)) return new MoveEval(null, Evaluation.LOSS);
            else return new MoveEval(null, Evaluation.DRAW);
        } else {
            // Iterate through columns
            for (int col = 0; col < board[0].length; col++) {
                int row = findFirstEmptyRow(board, col);
                if (row != -1) {
                    // Simulate the move
                    long[][] possBoard = copyBoard(board);
                    possBoard[row][col] = toMove;
                    MoveEval tempMR = findMove(possBoard, (toMove == 1) ? -1 : 1);
                    Evaluation possEval = invertResult(tempMR.evaluation);

                    // Choose the best move
                    if (possEval.ordinal() > bestEval.ordinal() || (possEval == bestEval && Math.random() < 0.5)) {
                        bestEval = possEval;
                        bestMove = new Move(toMove, col, row);
                    }
                }
            }
            return new MoveEval(bestMove, bestEval);
        }
    }

    private int findFirstEmptyRow(long[][] board, int col) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return -1; // Column is full
    }

    private Evaluation invertResult(Evaluation in) {
        if (in == Evaluation.DRAW) return Evaluation.DRAW;
        else if (in == Evaluation.WIN) return Evaluation.LOSS;
        return Evaluation.WIN;
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

/*

@Override
    public void makeMove(long[][] board) {
        Move move = findMove(board, myPiece).move;
        board[move.getRow()][move.getCol()] = myPiece;
    }
    private MoveEval findMove(long[][] board, long toMove) {
        Move bestMove = null;
        Evaluation bestEval = Evaluation.LOSS;

        // Check for a win, loss, or draw situation first
        Long result = Connect4.getWinner(board);
        if (result != null) {
            if (result == toMove) return new MoveEval(null, Evaluation.WIN);
            else if (result == (0 - toMove)) return new MoveEval(null, Evaluation.LOSS);
            else return new MoveEval(null, Evaluation.DRAW);
        } else {
            // Iterate through columns instead of rows and columns
            for (int col = 0; col < board[0].length; col++) {
                // Find the lowest empty space in the column
                int row = findFirstEmptyRow(board, col);
                if (row != -1) {
                    // Simulate the move
                    long[][] possBoard = copyBoard(board);
                    possBoard[row][col] = toMove;
                    MoveEval tempMR = findMove(possBoard, (toMove == 1) ? -1 : 1);
                    Evaluation possEval = invertResult(tempMR.evaluation);

                    // Choose the best move
                    if (possEval.ordinal() > bestEval.ordinal() || (possEval == bestEval && Math.random() < 0.5)) {
                        bestEval = possEval;
                        bestMove = new Move(toMove, col, row);
                    }
                }
            }
            return new MoveEval(bestMove, bestEval);
        }
    }

    private int findFirstEmptyRow(long[][] board, int col) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return -1; // Column is full
    }

    private Evaluation invertResult(Evaluation in) {
        if (in == Evaluation.DRAW) return Evaluation.DRAW;
        else if (in == Evaluation.WIN) return Evaluation.LOSS;
        return Evaluation.WIN;
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

 */
}
