package amazons;
import org.junit.Test;

import static amazons.Piece.SPEAR;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/** Junit tests for our Board iterators.
 *  @author Rick Surya
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLE_FROM_TEST_SQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLE_FROM_TEST_SQUARES.size(), numSquares);
        assertEquals(REACHABLE_FROM_TEST_SQUARES.size(), squares.size());
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGAL_FROM_TEST_BOARD_2);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGAL_FROM_TEST_MOVES_2.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(LEGAL_FROM_TEST_MOVES_2.size(), numMoves);
        assertEquals(LEGAL_FROM_TEST_MOVES_2.size(), moves.size());
    }

    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD =
        {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
        };

    static final Set<Square> REACHABLE_FROM_TEST_SQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Piece[][] LEGAL_FROM_TEST_BOARD_1 =
        {
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, S, S, E, S, E, E, E},
            {E, E, E, S, S, W, S, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
        };

    static final Piece[][] LEGAL_FROM_TEST_BOARD_2 =
        {
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, S, E, E, S, E, E, E},
            {E, E, E, S, S, W, S, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
        };

    static final Set<Move> LEGAL_FROM_TEST_MOVES_1 =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq(5, 4), Square.sq(5, 5), Square.sq(5, 4))
            ));

    static final Set<Move> LEGAL_FROM_TEST_MOVES_2 =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq(5, 4), Square.sq(5, 5), Square.sq(5, 4)),
                    Move.mv(Square.sq(5, 4), Square.sq(5, 5), Square.sq(4, 5)),
                    Move.mv(Square.sq(5, 4), Square.sq(4, 5), Square.sq(5, 4)),
                    Move.mv(Square.sq(5, 4), Square.sq(4, 5), Square.sq(5, 5))
            ));
    static final Piece[][] LEGAL_FROM_TEST_BOARD_3 =
        {
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, S, E, E, S, E, E, E},
            {E, E, E, S, W, W, S, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
        };

    static final Set<Move> LEGAL_FROM_TEST_MOVES_3 =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq(5, 4), Square.sq(5, 5), Square.sq(5, 4)),
                    Move.mv(Square.sq(5, 4), Square.sq(5, 5), Square.sq(4, 5)),
                    Move.mv(Square.sq(5, 4), Square.sq(4, 5), Square.sq(5, 4)),
                    Move.mv(Square.sq(5, 4), Square.sq(4, 5), Square.sq(5, 5)),
                    Move.mv(Square.sq(4, 4), Square.sq(5, 5), Square.sq(4, 4)),
                    Move.mv(Square.sq(4, 4), Square.sq(5, 5), Square.sq(4, 5)),
                    Move.mv(Square.sq(4, 4), Square.sq(4, 5), Square.sq(4, 4)),
                    Move.mv(Square.sq(4, 4), Square.sq(4, 5), Square.sq(5, 5))
            ));
    static final Piece[][] LEGAL_FROM_TEST_BOARD_0 =
        {
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, S, W, W, S, E, E, E},
            {E, E, E, S, S, S, S, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
        };
    static final Set<Move> LEGAL_FROM_TEST_MOVES_0 =
            new HashSet<>(Arrays.asList());

}
