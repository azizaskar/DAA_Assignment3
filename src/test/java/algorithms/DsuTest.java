package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DsuTest {

    @Test
    void testBasicDsuOperations() {
        Metrics metrics = new Metrics();
        DSU dsu = new DSU(5, metrics);

        assertEquals(5, dsu.components());
        assertTrue(dsu.union(0, 1));
        assertTrue(dsu.union(2, 3));
        assertTrue(dsu.union(0, 2));
        assertEquals(2, dsu.components());
        assertEquals(dsu.find(1), dsu.find(3));
        assertFalse(dsu.union(1, 3)); // уже в одном множестве
    }

    @Test
    void testGraphFilesLoading() throws IOException {
        Graph connected = Graph.fromFile(Path.of("data/small_connected.txt"));
        assertEquals(5, connected.getVertexCount());
        assertEquals(7, connected.getEdgeCount());

        Graph disconnected = Graph.fromFile(Path.of("data/small_disconnected.txt"));
        assertEquals(6, disconnected.getVertexCount());
        assertTrue(disconnected.getNeighbors(5).isEmpty()); // изолированная вершина
    }
}