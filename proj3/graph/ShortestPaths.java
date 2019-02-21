package graph;

import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Rick Surya
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
    }

    /** Initialize the shortest paths. Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        TreeSet<Integer> fringe = new TreeSet<>(new VerticeComparator());
        for (int v : _G.vertices()) {
            setWeight(v, Double.MAX_VALUE);
            setPredecessor(v, 0);
            fringe.add(v);
        }
        setWeight(_source, 0);
        fringe.remove(Integer.valueOf(_source));
        fringe.add(Integer.valueOf(_source));
        while (!fringe.isEmpty()) {
            int v = fringe.pollFirst();
            if (v == _dest) {
                break;
            }
            for (int i : _G.successors(v)) {
                if (getWeight(v) + getWeight(v, i) < getWeight(i)) {
                    fringe.remove(Integer.valueOf(i));
                    setWeight(i, getWeight(v) + getWeight(v, i));
                    fringe.add(Integer.valueOf(i));
                    setPredecessor(i, v);
                }
            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        ArrayList<Integer> result = new ArrayList<>();
        while (v != 0) {
            result.add(v);
            v = getPredecessor(v);
        }
        Collections.reverse(result);
        return result;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** Comparator for fringe. */
    private class VerticeComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer v1, Integer v2) {
            double w1 = getWeight(v1) + estimatedDistance(v1);
            double w2 = getWeight(v2) + estimatedDistance(v2);
            if (w1 > w2) {
                return 1;
            } else if (w1 < w2) {
                return -1;
            } else {
                return 0;
            }
        }
    }


    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;

}
