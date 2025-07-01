package spw4.connectfour;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.junit.jupiter.*;

import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectFourImplTests {

    @BeforeEach
    void setUp() {

    }

    static Player[] validPlayers() {
        return new Player[] { Player.red, Player.yellow };
    }
    @ParameterizedTest
    @MethodSource("validPlayers")
    void testBaseConstructor_withValidPlayers(Player playerOnTurn) {
        ConnectFourImpl connectFour = new ConnectFourImpl(playerOnTurn);
        assertEquals(playerOnTurn, connectFour.getCurrentPlayer());
        assertFalse(connectFour.isGameOver());
        assertEquals(Player.none, connectFour.getWinner());
    }

    static Player[] invalidPlayers() {
        return new Player[] { null, Player.none };
    }
    @ParameterizedTest
    @MethodSource("invalidPlayers")
    void testBaseConstructor_withInvalidPlayers(Player playerOnTurn) {
        assertThatThrownBy(() -> new ConnectFourImpl(playerOnTurn))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No Player");
    }

    @ParameterizedTest
    @MethodSource("validPlayers")
    void testConstructorWithBoard_withValidPlayers(Player playerOnTurn) {
        Player[][] players = {
                {Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none}
        };

        ConnectFourImpl connectFour = new ConnectFourImpl(players, playerOnTurn);
        assertEquals(playerOnTurn, connectFour.getCurrentPlayer());
        assertFalse(connectFour.isGameOver());
        assertEquals(Player.none, connectFour.getWinner());
    }

    @ParameterizedTest
    @MethodSource("invalidPlayers")
    void testConstructorWithBoard_withInvalidPlayers(Player playerOnTurn) {
        assertThatThrownBy(() -> new ConnectFourImpl(null, playerOnTurn))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No Player");
    }

    @Test
    void testGetPlayerAt_ChecksSameIndexAsBoard() {
        var board = mock(Board.class);
        when(board.getPlayerAt(1, 1))
                .thenReturn(Player.red);
        when(board.getPlayerAt(2, 3))
                .thenReturn(Player.yellow);
        when(board.getPlayerAt(Integer.MIN_VALUE, Integer.MAX_VALUE))
                .thenReturn(Player.yellow);

        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        connectFour.board = board;


        assertEquals(Player.red, connectFour.getPlayerAt(1,1));
        assertEquals(Player.yellow, connectFour.getPlayerAt(2,3));
        assertEquals(Player.yellow, connectFour.getPlayerAt(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    void testGetCurrentPlayer_afterStart() {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        assertEquals(Player.red, connectFour.getCurrentPlayer());

        connectFour = new ConnectFourImpl(Player.yellow);
        assertEquals(Player.yellow, connectFour.getCurrentPlayer());
    }

    @Test
    void testGameOver_onEmptyBoard_returnsFalse() {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        assertFalse(connectFour.isGameOver());
    }

    @Test
    void testGameOver_withWinnerHorizontal_returnsTrue() {
        // Find horizontal on left
        Player[][] arr = new Player[][]{
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.red, Player.red, Player.red, Player.red, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
        };
        ConnectFourImpl connectFour = new ConnectFourImpl(arr, Player.red);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.red, connectFour.getWinner());

        // Find horizontal on right
        arr = new Player[][]{
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.red, Player.red, Player.red, Player.red},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
        };
        connectFour = new ConnectFourImpl(arr, Player.red);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.red, connectFour.getWinner());
    }

    @Test
    void testGameOver_withWinnerVertical_returnsTrue() {
        // Find horizontal on left
        Player[][] arr = new Player[][]{
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
        };
        ConnectFourImpl connectFour = new ConnectFourImpl(arr, Player.yellow);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.yellow, connectFour.getWinner());

        // Find horizontal on right
        arr = new Player[][]{
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
        };
        connectFour = new ConnectFourImpl(arr, Player.yellow);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.yellow, connectFour.getWinner());
    }

    @Test
    void testGameOver_withWinnerDiagonalDown_returnsTrue() {
        // Find diagonal on left
        Player[][] arr = new Player[][]{
                {Player.red, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.red, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.red, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.red, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
        };
        ConnectFourImpl connectFour = new ConnectFourImpl(arr, Player.red);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.red, connectFour.getWinner());

        // Find diagonal on right
        arr = new Player[][]{
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.red, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.red, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.red, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.red},
        };
        connectFour = new ConnectFourImpl(arr, Player.red);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.red, connectFour.getWinner());
    }

    @Test
    void testGameOver_withWinnerDiagonalUp_returnsTrue() {
        // Find diagonal on left
        Player[][] arr = new Player[][]{
                {Player.yellow, Player.none, Player.none, Player.red, Player.none, Player.none, Player.none},
                {Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
        };
        ConnectFourImpl connectFour = new ConnectFourImpl(arr, Player.yellow);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.yellow, connectFour.getWinner());

        // Find diagonal on right
        arr = new Player[][]{
                {Player.none, Player.none, Player.none, Player.red, Player.none, Player.none, Player.yellow},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.yellow, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.yellow, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.yellow, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.none, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
        };
        connectFour = new ConnectFourImpl(arr, Player.yellow);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.yellow, connectFour.getWinner());
    }

    @Test
    void testGameOver_withFullBoard_returnsTrue_noWinner() {
        Player[][] arr = new Player[][]{
                {Player.red, Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow, Player.red},
                {Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow},
                {Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow},
                {Player.red, Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow, Player.red},
                {Player.red, Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow, Player.red},
                {Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow, Player.red, Player.yellow}
        };
        ConnectFourImpl connectFour = new ConnectFourImpl(arr, Player.yellow);

        assertTrue(connectFour.isGameOver());
        assertEquals(Player.none, connectFour.getWinner());
    }

    @Test
    void testGameOver_withWinnerHorizontal_StepByStep_returnsTrue () {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        connectFour.drop(0);
        connectFour.drop(0);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(1);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(2);
        connectFour.drop(2);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(3);
        assertTrue(connectFour.isGameOver());
        assertEquals(Player.red, connectFour.getWinner());


    }

    @Test
    void testGameOver_withWinnerVertical_StepByStep_returnsTrue () {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        connectFour.drop(0);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(0);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(0);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(0);
        assertTrue(connectFour.isGameOver());
        assertEquals(Player.red, connectFour.getWinner());


    }

    @Test
    void testGameOver_withWinnerDiagonalDown_StepByStep_returnsTrue () {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        connectFour.drop(0);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(1);
        connectFour.drop(2);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(2);
        connectFour.drop(3);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(2);
        connectFour.drop(3);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(3);
        connectFour.drop(6);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(3);
        assertTrue(connectFour.isGameOver());
        assertEquals(Player.red, connectFour.getWinner());


    }

    @Test
    void testGameOver_withWinnerDiagonalUp_StepByStep_returnsTrue () {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        connectFour.drop(0);
        connectFour.drop(0);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(0);
        connectFour.drop(0);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(1);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(2);
        connectFour.drop(1);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(6);
        connectFour.drop(2);
        assertFalse(connectFour.isGameOver());

        connectFour.drop(6);
        connectFour.drop(3);
        assertTrue(connectFour.isGameOver());
        assertEquals(Player.yellow, connectFour.getWinner());
    }

    @Test
    void testDrop_SwitchesPlayer_OnSuccess() {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);

        connectFour.drop(0);
        assertEquals(Player.yellow, connectFour.getCurrentPlayer());

        connectFour.drop(0);
        assertEquals(Player.red, connectFour.getCurrentPlayer());

        connectFour.drop(0);
        assertEquals(Player.yellow, connectFour.getCurrentPlayer());
    }

    @Test
    void testDrop_DoesNotSwitchPlayer_OnFullColumn() {
        Player[][] arr = new Player[][]{
                {Player.red, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.red, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.red, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.red, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.red, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
                {Player.red, Player.none, Player.none, Player.none, Player.none, Player.none, Player.none},
        };
        ConnectFourImpl connectFour = new ConnectFourImpl(arr, Player.yellow);

        // Drop in full Column
        connectFour.drop(0);
        assertEquals(Player.yellow, connectFour.getCurrentPlayer());

        connectFour.drop(0);
        assertEquals(Player.yellow, connectFour.getCurrentPlayer());

        connectFour.drop(0);
        assertEquals(Player.yellow, connectFour.getCurrentPlayer());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5 -1, 7, 10, Integer.MAX_VALUE})
    void testDrop_DoesNotSwitchPlayer_WhenOutOfBounds(int index) {

        ConnectFourImpl connectFour = new ConnectFourImpl(Player.yellow);

        // Drop in full Column
        assertThatThrownBy(() -> connectFour.drop(index))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Column out of bounds");
    }

    @Test
    void testReset() {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        connectFour.drop(0);
        connectFour.drop(0);
        connectFour.drop(0);
        connectFour.drop(0);
        assertEquals(Player.red, connectFour.getCurrentPlayer());

        connectFour.reset(Player.yellow);
        assertEquals(Player.yellow, connectFour.getCurrentPlayer());

       Player[][] board = connectFour.board.getBoard();
       for (int i = 0; i < connectFour.board.height; i++) {
           for (int j = 0; j < connectFour.board.width; j++) {
               assertEquals(Player.none, board[i][j]);
           }
       }
    }


    @Test
    void testToString() {
        ConnectFourImpl connectFour = new ConnectFourImpl(Player.red);
        String[] string = connectFour.toString().split("\n");
        assertTrue(string[1].contains("RED"));

        connectFour = new ConnectFourImpl(Player.yellow);
        string = connectFour.toString().split("\n");
        assertTrue(string[1].contains("YELLOW"));
    }


}
