package graph;

import java.util.ArrayList;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Rick Surya
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        int result = 0;
        for (int[] i : edges()) {
            if (i[1] == v) {
                result++;
            }
        }
        return result;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int[] i : edges()) {
            if (i[1] == v) {
                result.add(i[0]);
            }
        }

        return Iteration.iteration(result.iterator());
    }
}
