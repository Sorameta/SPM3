package spw4.connectfour;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTests {

    @Test
    public void testBoardStandardConstructor_cratesEmptyBoard() {
        Board board = new Board();

        Player[][] createdBoard = board.getBoard();
        for (int i = 0; i < createdBoard.length; i++) {
            for (int j = 0; j < createdBoard[i].length; j++) {
                assertEquals(Player.none, createdBoard[i][j]);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 10})
    public void testBoardConstructor_witSize_cratesEmptyBoard(int size) {
        Board board = new Board(size, size);

        Player[][] createdBoard = board.getBoard();
        for (int i = 0; i < createdBoard.length; i++) {
            for (int j = 0; j < createdBoard[i].length; j++) {
                assertEquals(Player.none, createdBoard[i][j]);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5, -1, 1, 2, 3,})
    public void testBoardConstructor_witInvalidHeight_throwsException(int height) {
        assertThatThrownBy(() -> new Board(height, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("greater than 4");
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5, -1, 1, 2, 3,})
    public void testBoardConstructor_witInvalidWidth_throwsException(int width) {
        assertThatThrownBy(() -> new Board(4, width))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("greater than 4");
    }

    @Test
    public void testBoardConstructor_withBoard_cratesBoardWithEqualFields() {
        Player[][] players = {
            {Player.red, Player.red, Player.red, Player.red},
            {Player.yellow, Player.yellow, Player.yellow, Player.yellow},
            {Player.none, Player.none, Player.none, Player.none},
            {Player.none, Player.none, Player.none, Player.none},
        };

        Board board = new Board(players);

        Player[][] createdBoard = board.getBoard();
        int height = createdBoard.length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < createdBoard[i].length; j++) {
                assertEquals(players[i][j], createdBoard[height-i-1][j]);
            }
        }
    }

    @Test
    public void testBoardConstructor_withNoBoard_throwsException() {
        assertThatThrownBy(() -> new Board(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No Board provided");
    }


    static Stream<Player[][]> invalidPlayerArrays() {
        return Stream.of(
                new Player[][] {
                        {Player.none, Player.none, Player.none},
                        {Player.none, Player.none, Player.none},
                        {Player.none, Player.none, Player.none},
                },
                new Player[][] {
                        {Player.none, Player.none, Player.none},
                        {Player.none, Player.none, Player.none},
                        {Player.none, Player.none, Player.none},
                        {Player.none, Player.none, Player.none},
                },
                new Player[][] {
                        {Player.none, Player.none, Player.none, Player.none},
                        {Player.none, Player.none, Player.none ,Player.none},
                        {Player.none, Player.none, Player.none ,Player.none},
                }
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPlayerArrays")
    public void testBoardConstructor_withShortBoard_throwsException(Player[][] players) {
        assertThatThrownBy(() -> new Board(players))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("greater than 4");
    }


    @Test
    public void testGetSizeofBoard() {
        Board board = new Board();
        assertEquals(6, board.getHeight());
        assertEquals(7, board.getWidth());

        board = new Board(10,10);
        assertEquals(10, board.getHeight());
        assertEquals(10, board.getWidth());
    }

    @Test
    public void testGetPlayerWithinBoard() {
        Player[][] players = {
                {Player.red, Player.red, Player.red, Player.red},
                {Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none},
                {Player.yellow, Player.yellow, Player.yellow, Player.yellow},
        };

        Board board = new Board(players);


        Player[][] createdBoard = board.getBoard();
        int height = createdBoard.length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < createdBoard[i].length; j++) {
                assertEquals(players[i][j], board.getPlayerAt(height-i-1,j));
            }
        }
    }


    @Test
    public void testGetPlayerOutsideBorders_returnsPlayerNone() {
        Player[][] players = {
                {Player.red, Player.red, Player.red, Player.red},
                {Player.red, Player.red, Player.red, Player.red},
                {Player.red, Player.red, Player.red, Player.red},
                {Player.red, Player.red, Player.red, Player.red},
        };
        Board board = new Board(players);

        assertEquals(Player.none, board.getPlayerAt(-1,-1));
        assertEquals(Player.none, board.getPlayerAt(0,-1));
        assertEquals(Player.none, board.getPlayerAt(-1,0));

        assertEquals(Player.none, board.getPlayerAt(4,4));
        assertEquals(Player.none, board.getPlayerAt(4,3));
        assertEquals(Player.none, board.getPlayerAt(3,4));

        assertEquals(Player.none, board.getPlayerAt(4,-1));
        assertEquals(Player.none, board.getPlayerAt(4,0));
        assertEquals(Player.none, board.getPlayerAt(3,-1));

        assertEquals(Player.none, board.getPlayerAt(-1,4));
        assertEquals(Player.none, board.getPlayerAt(-1,3));
        assertEquals(Player.none, board.getPlayerAt(0,4));
    }


    @Test
    public void testDropCol_withinBounds_returnsRow() {
        Board board = new Board();

        int droppedCol;

        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(0, droppedCol);
        assertEquals(Player.red, board.getPlayerAt(droppedCol, 0));

        droppedCol = board.dropInCol(0, Player.yellow);
        assertEquals(1, droppedCol);
        assertEquals(Player.yellow, board.getPlayerAt(droppedCol, 0));
        assertEquals(Player.red, board.getPlayerAt(droppedCol - 1, 0));

        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(2, droppedCol);
        assertEquals(Player.red, board.getPlayerAt(droppedCol, 0));
        assertEquals(Player.yellow, board.getPlayerAt(droppedCol - 1, 0));
        assertEquals(Player.red, board.getPlayerAt(droppedCol - 2, 0));

        droppedCol = board.dropInCol(6, Player.yellow);
        assertEquals(0, droppedCol);
        assertEquals(Player.yellow, board.getPlayerAt(droppedCol, 6));
    }


    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5, -1, 7, 10, Integer.MAX_VALUE})
    public void testDropCol_ColumnOutOfBounds_throwsException(int column) {
        Board board = new Board();

        assertThatThrownBy(() -> board.dropInCol(column, Player.red))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Column out of bounds");
    }

    @Test
    public void testDropCol_ColumnIsFull_returnsNegative() {
        Board board = new Board();

        int droppedCol;

        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(0, droppedCol);
        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(1, droppedCol);
        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(2, droppedCol);
        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(3, droppedCol);
        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(4, droppedCol);
        droppedCol = board.dropInCol(0, Player.red);
        assertEquals(5, droppedCol);

        droppedCol = board.dropInCol(0, Player.yellow);
        assertEquals(-1, droppedCol);
    }

    @Test
    public void testToString() {
        Player[][] players = {
                {Player.red, Player.none, Player.none, Player.yellow},
                {Player.none, Player.red, Player.yellow, Player.none},
                {Player.none, Player.yellow, Player.red, Player.none},
                {Player.yellow, Player.none, Player.none, Player.red},
        };

        Board board = new Board(players);

        String regex1 = "^[^RY.]*R\\s*\\.\\s*\\.\\s*Y[^RY.]*$";
        String regex2 = "^[^RY.]*\\.\\s*R\\s*Y\\s*\\.[^RY.]*$";
        String regex3 = "^[^RY.]*\\.\\s*Y\\s*R\\s*\\.[^RY.]*$";
        String regex4 = "^[^RY.]*Y\\s*\\.\\s*\\.\\s*R[^RY.]*$";

        String[] regex = {regex1, regex2, regex3, regex4};

        String[] rows = board.toString().split("\n");
        for (int i = 0; i < rows.length; i++) {
            rows[i] = rows[i].replaceAll("|", "");
            assertTrue(rows[i].matches(regex[i]));
        }

    }

}
