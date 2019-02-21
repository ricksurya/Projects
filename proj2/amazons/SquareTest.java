package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** Tests for Square.java
 *  @author Rick Surya
 */
public class SquareTest {

    public static void main(String[] ignored) {
        textui.runClasses(SquareTest.class);
    }

    /** Tests queenMove method. */
    @Test
    public void testQueenMove() {
        assertEquals(Square.sq(3, 5), Square.sq(3, 3).queenMove(0, 2));
        assertEquals(Square.sq(6, 6), Square.sq(3, 3).queenMove(1, 3));
        assertEquals(Square.sq(6, 3), Square.sq(3, 3).queenMove(2, 3));
    }

    /** Tests direction method. */
    @Test
    public void testDirection() {
        assertEquals(0, Square.sq(3, 3).direction(Square.sq(3, 6)));
        assertEquals(1, Square.sq(3, 3).direction(Square.sq(5, 5)));
        assertEquals(2, Square.sq(3, 3).direction(Square.sq(4, 3)));
        assertEquals(3, Square.sq(3, 3).direction(Square.sq(4, 2)));
        assertEquals(4, Square.sq(3, 3).direction(Square.sq(3, 1)));
        assertEquals(5, Square.sq(3, 3).direction(Square.sq(1, 1)));
        assertEquals(6, Square.sq(3, 3).direction(Square.sq(1, 3)));
        assertEquals(7, Square.sq(3, 3).direction(Square.sq(2, 4)));
    }
    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   B - - - - - - - - B\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   W - - - - - - - - W\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
            + "   - S S S - - S S S -\n"
            + "   - S - S - - S - S -\n"
            + "   - S S S - - S S S -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - W - - - - W - -\n"
            + "   - - - W W W W - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n";

}
