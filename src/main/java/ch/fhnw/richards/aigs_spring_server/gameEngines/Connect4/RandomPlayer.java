package ch.fhnw.richards.aigs_spring_server.gameEngines.Connect4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPlayer implements C4_AI {

    @Override
    public void makeMove(long[][] board) {
        List<Integer> availableColumns = new ArrayList<>();
        for (int col = 0; col < board[0].length; col++) {
            if (board[0][col] == 0) { // Check if the top of the column is empty
                availableColumns.add(col);
            }
        }

        if (!availableColumns.isEmpty()) {
            Random rand = new Random();
            int col = availableColumns.get(rand.nextInt(availableColumns.size()));
            int row = findFirstEmptyRow(board, col);
            board[row][col] = -1; // Assuming -1 is the AI's piece
        }
    }

    private int findFirstEmptyRow(long[][] board, int col) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                return row;
            }
        }
        return -1; // Should not happen if called correctly
    }
}
