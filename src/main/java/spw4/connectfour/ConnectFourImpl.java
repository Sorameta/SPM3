package spw4.connectfour;

public class ConnectFourImpl implements ConnectFour {
    public Board board;
    private Player currentPlayer;
    private boolean isGameOver;
    private Player winner;


    public ConnectFourImpl(Player playerOnTurn) {
        if (playerOnTurn == null || playerOnTurn == Player.none) {
            throw new IllegalArgumentException("No Player provided");
        }
        this.currentPlayer = playerOnTurn;
        this.board = new Board();
        this.winner = Player.none;
    }

    public ConnectFourImpl(Player[][] board, Player playerOnTurn) {
        if (playerOnTurn == null || playerOnTurn == Player.none) {
            throw new IllegalArgumentException("No Player provided");
        }
        this.currentPlayer = playerOnTurn;
        this.board = new Board(board);
        this.winner = Player.none;
    }

    public Player getPlayerAt(int row, int col) {
        return board.getPlayerAt(row, col);
    }

    @Override
    public Player getPlayerOnTurn() {
        return currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        if (winner == Player.none) {
            checkForGameOver();
        }
        return isGameOver;
    }

    public Player getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nPlayer: ");
        switch (currentPlayer) {
            case red:
                sb.append("RED");
                break;
            case yellow:
                sb.append("YELLOW");
                break;
            default:
                break;
        }
        sb.append("\n");
        sb.append(board);

        return sb.toString();
    }

    public void reset(Player playerOnTurn) {
        this.currentPlayer = playerOnTurn;
        this.board = new Board();
    }

    public void drop(int col) {
        int row = board.dropInCol(col, currentPlayer);
        if (row != -1) {
            checkForGameOver();
            if (currentPlayer == Player.red) {
                currentPlayer = Player.yellow;
            } else {
                currentPlayer = Player.red;
            }
        }
    }

    private void checkForGameOver() {
        // Horizontal Check
        for (int r = 0; r < board.getHeight(); r++) {
            for (int c = 0; c <= board.getWidth() - 4; c++) {
                boolean win = true;
                for (int i = 0; i < 4; i++) {
                    if (board.getPlayerAt(r, c + i) != currentPlayer) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    isGameOver = true;
                    winner = currentPlayer;
                    return;
                }
            }
        }

        // Vertical Check
        for (int c = 0; c < board.getWidth(); c++) {
            for (int r = 0; r <= board.getHeight() - 4; r++) {
                boolean win = true;
                for (int i = 0; i < 4; i++) {
                    if (board.getPlayerAt(r + i, c) != currentPlayer) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    isGameOver = true;
                    winner = currentPlayer;
                    return;
                }
            }
        }

        // Diagonal (Bottom-Left to Top-Right)
        for (int r = 0; r <= board.getHeight() - 4; r++) {
            for (int c = 0; c <= board.getWidth() - 4; c++) {
                boolean win = true;
                for (int i = 0; i < 4; i++) {
                    if (board.getPlayerAt(r + i, c + i) != currentPlayer) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    isGameOver = true;
                    winner = currentPlayer;
                    return;
                }
            }
        }

        // Diagonal (Top-Left to Bottom-Right)
        for (int r = 3; r < board.getHeight(); r++) {
            for (int c = 0; c <= board.getWidth() - 4; c++) {
                boolean win = true;
                for (int i = 0; i < 4; i++) {
                    if (board.getPlayerAt(r - i, c + i) != currentPlayer) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    isGameOver = true;
                    winner = currentPlayer;
                    return;
                }
            }
        }

        // Check if board is full
        for (int r = 0; r < board.getHeight(); r++) {
            for (int c = 0; c < board.getWidth(); c++) {
                if (board.getPlayerAt(r, c) == Player.none) {
                    isGameOver = false;
                    return;
                }
            }
        }
        isGameOver = true;
    }
}
