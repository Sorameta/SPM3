package spw4.connectfour;

import static spw4.connectfour.Player.*;

public class Board {
    private final String ERROR_BOARD_SIZE = "Number of rows and columns must be greater than 4";

    Player[][] board;
    int height;
    int width;

    public Board() {
        this.width = 7;
        this.height = 6;
        this.board = new Player[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = none;
            }
        }
    }

    public Board(int height, int width) {
        if (height < 4 || width < 4) {
            throw new IllegalArgumentException(ERROR_BOARD_SIZE);
        }

        this.height = height;
        this.width = width;
        this.board = new Player[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = none;
            }
        }
    }

    public Board(Player[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("No Board provided");
        }
        if (board.length < 4 || board[0].length < 4) {
            throw new IllegalArgumentException(ERROR_BOARD_SIZE);
        }

        this.height = board.length;
        this.width = board[0].length;
        this.board = new Player[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.board[i][j] = board[height - i - 1][j];
            }
        }
    }

    public Player[][] getBoard() {
        return board;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Player getPlayerAt(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) {
            return none;
        }
        return board[row][col];
    }

    public int dropInCol(int col, Player player) {
        if (col < 0 || col >= width) {
            throw new IllegalArgumentException("Column out of bounds");
        }

        for (int row = 0 ; row < height; row++) {
            if (board[row][col] == none) {
                board[row][col] = player;
                return row;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = height - 1; i >= 0; i--) {
            sb.append("|");
            for (int j = 0; j < width; j++) {
                String player;
                switch (board[i][j]) {
                    case red:
                        player = "R";
                        break;
                    case yellow:
                        player = "Y";
                        break;
                    default:
                        player = ".";
                        break;
                }

                sb.append(String.format("%3s", player));
            }
            sb.append("  |\n");
        }
        return sb.toString();
    }

}
