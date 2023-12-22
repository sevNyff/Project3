package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

public class MinmaxPlayer implements C4_AI {
    private long myPiece = -1; // Which player are we?
    private static final int MAX_DEPTH = 5; // Set a limit to the recursion depth

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
        try {
            MoveEval moveEval = findBestMove(board, myPiece, 0);
            if (moveEval != null && moveEval.move != null) {
                int row = findFirstEmptyRow(board, moveEval.move.getCol());
                if (row != -1) {
                    board[row][moveEval.move.getCol()] = myPiece;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
    }

    private MoveEval findBestMove(long[][] board, long toMove, int depth) {
        if (depth > MAX_DEPTH) {
            return new MoveEval(null, Evaluation.DRAW); // Stop at max depth
        }

        Move bestMove = null;
        Evaluation bestEval = Evaluation.LOSS;

        for (int col = 0; col < board[0].length; col++) {
            int row = findFirstEmptyRow(board, col);
            if (row != -1) {
                long[][] tempBoard = copyBoard(board);
                tempBoard[row][col] = toMove;

                // Check if the current move is a winning move before proceeding
                if (Connect4.getWinner(tempBoard) == toMove) {
                    return new MoveEval(new Move(toMove, col, row), Evaluation.WIN);
                }

                MoveEval tempEval = findBestMove(tempBoard, -toMove, depth + 1);
                Evaluation currentEval = invertResult(tempEval.evaluation);

                if (currentEval.ordinal() > bestEval.ordinal() || (currentEval == bestEval && Math.random() < 0.5)) {
                    bestEval = currentEval;
                    bestMove = new Move(toMove, col, row);
                }
            }
        }
        return new MoveEval(bestMove, bestEval);
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
}
