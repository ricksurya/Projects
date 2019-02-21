package amazons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Collections;

import static amazons.Piece.*;


/** The state of an Amazons Game.
 *  @author Rick Surya
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        _turn = model._turn;
        _winner = model._winner;
        _board = new Piece[10][10];
        _numMoves = model._numMoves;
        _moves = model._moves;
        _black = model._black;
        _white = model._white;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                _board[i][j] = model._board[i][j];
            }
        }
    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = WHITE;
        _winner = EMPTY;
        _board = makeBoard();
        _numMoves = 0;
        _moves = new LinkedList<>();
    }

    /** Helper for initializing a board.
     * @return The board representation. */
    Piece[][] makeBoard() {
        Piece[][] result = new Piece[10][10];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result[i][j] = EMPTY;
            }
        }
        _white = new ArrayList<>(Arrays.asList(Square.sq(0, 3), Square.sq(3, 0),
                Square.sq(6, 0), Square.sq(9, 3)));
        _black = new ArrayList<>(Arrays.asList(Square.sq(0, 6), Square.sq(3, 9),
                Square.sq(6, 9), Square.sq(9, 6)));
        for (Square s : _white) {
            result[s.col()][s.row()] = WHITE;
        }
        for (Square s : _black) {
            result[s.col()][s.row()] = BLACK;
        }
        return result;
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _numMoves;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        Iterator<Move> turn = legalMoves(turn());
        if (!turn.hasNext()) {
            _winner = _turn.opponent();
            return _winner;
        }
        return null;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return _board[s.col()][s.row()];
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return _board[col][row];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        put(p, s.col(), s.row());
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        _board[col][row] = p;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        Square curr = from;
        int[] dir = Square.dirHelper(from.direction(to));
        while (curr != to) {
            curr = Square.sq(curr.col() + dir[0], curr.row() + dir[1]);
            if (get(curr.col(), curr.row()) != EMPTY && curr != asEmpty) {
                return false;
            }
        }
        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return _board[from.col()][from.row()] == turn();
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isLegal(from) && isUnblockedMove(from, to, null);
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from) && isUnblockedMove(from, to, null)
                && isUnblockedMove(to, spear, from);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        if (move == null) {
            return false;
        }
        return isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        _moves.addLast(Move.mv(from, to, spear));
        Piece moved = get(from);
        put(moved, to);
        put(EMPTY, from);
        put(SPEAR, spear);
        _numMoves++;
        _turn = _turn.opponent();
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        Move last = _moves.getLast();
        Square spear = last.spear();
        Square toQueen = last.to();
        Square fromQueen = last.from();
        _turn = _turn.opponent();
        put(EMPTY, toQueen);
        put(EMPTY, spear);
        put(_turn, fromQueen);
        _moves.removeLast();
        _numMoves--;
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Square result = _from.queenMove(_dir, _steps);
            toNext();
            return result;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            _steps++;
            Square to = _from.queenMove(_dir, _steps);
            while (hasNext() && (_from.queenMove(_dir, _steps) == null
                    || !isUnblockedMove(_from,
                    _from.queenMove(_dir, _steps), _asEmpty))) {
                _dir++;
                _steps = 1;
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;

    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            _startingSquares = startHelper();
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _spearThrows.hasNext();
        }

        @Override
        public Move next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Move result = Move.mv(_start, _nextSquare, _spearThrows.next());
            toNext();
            return result;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (!_spearThrows.hasNext()) {
                if (!_startingSquares.hasNext()
                        && !_pieceMoves.hasNext()) {
                    return;
                } else {
                    while (!_pieceMoves.hasNext()
                            && _startingSquares.hasNext()) {
                        _start = _startingSquares.next();
                        _pieceMoves = new ReachableFromIterator(_start, null);
                    }
                    if (_pieceMoves.hasNext()) {
                        _nextSquare = _pieceMoves.next();
                        _spearThrows = new ReachableFromIterator(_nextSquare,
                                _start);
                    }
                }
            }

        }

        /** Returns a list of squares containing _fromPiece. */
        private Iterator<Square> startHelper() {
            ArrayList<Square> result = new ArrayList<Square>();
            for (int c = 0; c < SIZE; c++) {
                for (int r = 0; r < SIZE; r++) {
                    if (_board[c][r] == _fromPiece) {
                        result.add(Square.sq(c, r));
                    }
                }
            }
            return result.iterator();
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
    }

    @Override
    public String toString() {
        String result = "";
        for (int row = SIZE - 1; row >= 0; row--) {
            for (int col = 0; col < SIZE; col++) {
                if (col == 0) {
                    result += "   ";
                }
                result += get(col, row).toString();
                if (col != SIZE - 1) {
                    result += " ";
                }
            }
            result += '\n';
        }
        return result;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;

    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** Representation of the board. */
    private Piece[][] _board;

    /** Number of moves that have not been undone. */
    private int _numMoves;

    /** List of moves represented by a Linked List. */
    private LinkedList<Move> _moves;

    /** Positions for white. */
    private ArrayList<Square> _white;

    /** Positions for black. */
    private ArrayList<Square> _black;

}
