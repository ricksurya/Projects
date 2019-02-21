package graph;
import java.util.ArrayList;
import java.util.Arrays;


/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Rick Surya
 */
abstract class GraphObj extends Graph {

    /** A new, empty Graph. */
    GraphObj() {
        _vertexes = new ArrayList<>();
        _edges = new ArrayList<>();
    }

    @Override
    public int vertexSize() {
        return _vertexes.size();
    }

    @Override
    public int maxVertex() {
        return _vertexes.get(vertexSize() - 1);
    }

    @Override
    public int edgeSize() {
        return _edges.size();
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        int result = 0;
        for (int[] i : _edges) {
            if (i[0] == v) {
                result++;
            } else if (!isDirected()) {
                if (i[1] == v) {
                    result++;
                }
            }
        }
        return result;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        return _vertexes.contains(Integer.valueOf(u));
    }

    @Override
    public boolean contains(int u, int v) {
        if (!(contains(u) && contains(v))) {
            return false;
        }
        for (int[] i : _edges) {
            if (i[0] == u && i[1] == v) {
                return true;
            }
            if (!isDirected()) {
                if (i[0] == v && i[1] == u) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int add() {
        int result = 1;
        while (result <= vertexSize() && result == _vertexes.get(result - 1)) {
            result++;
        }
        _vertexes.add(result - 1, result);
        return result;
    }

    @Override
    public int add(int u, int v) {
        checkMyVertex(u);
        checkMyVertex(v);
        int id = edgeId(u, v);
        if (contains(u, v)) {
            return id;
        } else {
            _edges.add(new int[]{u, v});
            return id;
        }
    }

    @Override
    public void remove(int v) {
        if (contains(v)) {
            _vertexes.remove(Integer.valueOf(v));
            _edges.removeIf((x -> x[0] == v || x[1] == v));
        }
    }

    @Override
    public void remove(int u, int v) {
        if (contains(u) && contains(v)) {
            _edges.removeIf((x -> Arrays.equals(x, new int[] {u, v})));
            if (!isDirected()) {
                _edges.removeIf((x -> Arrays.equals(x, new int[] {v, u})));
            }
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        return Iteration.iteration(_vertexes.iterator());
    }

    @Override
    public Iteration<Integer> successors(int v) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int[] i : _edges) {
            if (i[0] == v) {
                result.add(i[1]);
            } else if (!isDirected()) {
                if (i[1] == v) {
                    result.add(i[0]);
                }
            }
        }

        return Iteration.iteration(result.iterator());
    }


    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        return Iteration.iteration(_edges.iterator());
    }

    @Override
    protected void checkMyVertex(int v) {
        if (!contains(v)) {
            throw new IllegalArgumentException("Integer v "
                    + "is not a vertex of the graph");
        }
    }

    /** Source: Cantor pairing function from Wikipedia. */
    @Override
    protected int edgeId(int u, int v) {
        return ((u + v) * (u + v + 1)) / 2 + v;
    }

    /** Represents the vertex points in the graph. */
    private ArrayList<Integer> _vertexes;

    /** Represents the edges in the graph. */
    private ArrayList<int[]> _edges;

}
