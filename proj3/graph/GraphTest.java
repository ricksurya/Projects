package graph;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Rick Surya
 */


public class GraphTest {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void testMaxVertex() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.remove(2);
        assertEquals(3, g.maxVertex());
    }


    @Test
    public void testUndirectedAddVert() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        assertEquals(1, g.vertexSize());
        g.add();
        assertEquals(2, g.vertexSize());
        assertEquals(3, g.add());
    }

    @Test
    public void testUndirectedAddEdge() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        assertEquals(1, g.edgeSize());
        g.add(1, 3);
        assertEquals(2, g.edgeSize());
        g.add(1, 2);
        assertEquals(2, g.edgeSize());
    }

    @Test
    public void testUndirectedOutDegree() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        assertEquals(1, g.outDegree(1));
        assertEquals(0, g.outDegree(3));
        g.add(1, 3);
        assertEquals(2, g.outDegree(1));
        assertEquals(1, g.outDegree(2));
    }

    @Test
    public void testDirectedOutDegree() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        assertEquals(1, g.outDegree(1));
        assertEquals(0, g.outDegree(3));
        g.add(1, 3);
        assertEquals(2, g.outDegree(1));
        assertEquals(0, g.outDegree(2));
        assertEquals(1, g.inDegree(2));
        UndirectedGraph h = new UndirectedGraph();
        h.add();
        h.add();
        h.add();
        h.add(1, 2);
        h.add(1, 3);
        h.add(3, 1);
        assertEquals(2, h.edgeSize());
    }

    @Test
    public void testContainEdge() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        assertTrue(g.contains(1, 3));
        assertTrue(g.contains(1, 2));
        assertFalse(g.contains(3, 1));
        UndirectedGraph h = new UndirectedGraph();
        h.add();
        h.add();
        h.add();
        h.add(1, 2);
        h.add(1, 3);
        assertTrue(h.contains(1, 3));
        assertTrue(h.contains(1, 2));
        assertTrue(h.contains(3, 1));
    }

    @Test
    public void testRemoveVertex() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(3, 1);
        g.remove(1);
        assertEquals(2, g.vertexSize());
        assertEquals(0, g.edgeSize());
    }

    @Test
    public void testRemoveEdge() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(3, 1);
        g.remove(1, 3);
        assertEquals(2, g.edgeSize());
        assertEquals(3, g.vertexSize());
        UndirectedGraph h = new UndirectedGraph();
        h.add();
        h.add();
        h.add();
        h.add(1, 2);
        h.add(3, 1);
        h.remove(1, 3);
        assertEquals(1, h.edgeSize());
    }

    @Test
    public void testInDegree() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(3, 1);
        assertEquals(1, g.inDegree(1));
        assertEquals(1, g.outDegree(1));
        g.add(2, 1);
        assertEquals(2, g.inDegree(1));
    }

}

