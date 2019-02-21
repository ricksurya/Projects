package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;

public class BoardTest {
    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Tests isLegal. */
    @Test
    public void testIsLegal() {
        Board b = new Board();
        assertTrue(b.isLegal(Square.sq(0, 3)));
        assertFalse(b.isLegal(Square.sq(0, 6)));
        assertFalse(b.isLegal(Square.sq(0, 2)));
        assertTrue(b.isLegal(Square.sq(0, 3),
                Square.sq(1, 4), Square.sq(0, 3)));
    }

    /** Tests makeMove. */
    @Test
    public void testMakeMove() {
        Board b = new Board();
        Move m = Move.mv(Square.sq(3, 0), Square.sq(3, 1), Square.sq(3, 2));
        b.makeMove(m);
        assertEquals(WHITE, b.get(3, 1));
        assertEquals(SPEAR, b.get(3, 2));
        assertTrue(b.isLegal(Square.sq(0, 6)));
        assertEquals(1, b.numMoves());
        m = Move.mv(Square.sq(0, 5), Square.sq(0, 6), Square.sq(0, 7));
        b.makeMove(m);
        System.out.println(b.toString());
    }

    /** Test undo. */
    @Test
    public void testUndo() {
        Board b = new Board();
        Move m = Move.mv(Square.sq(0, 3), Square.sq(1, 4), Square.sq(0, 3));
        b.makeMove(m);
        b.undo();
        assertEquals(EMPTY, b.get(1, 4));
        assertEquals(WHITE, b.get(0, 3));
        assertEquals(0, b.numMoves());
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
