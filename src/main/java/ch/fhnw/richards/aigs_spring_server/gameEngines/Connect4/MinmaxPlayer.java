package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

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
    }

    private MoveEval findMove(long[][] board, long toMove) {
        int bestCol = -1;
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
            return new MoveEval(bestCol, bestEval);
        }
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

                if (isMaximizingPlayer && eval.ordinal() > bestEval.ordinal()) {
                    bestEval = eval;
                } else if (!isMaximizingPlayer && eval.ordinal() < bestEval.ordinal()) {
                    bestEval = eval;
                }
            }
        }

        return bestEval;
    }

    private int findFirstEmptyRow(long[][] board, int col) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return -1; // Column is full
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
