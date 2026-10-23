package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SccTest {

    @Test
    void testKosarajuOnCyclicGraph() throws IOException {
        Graph graph = Graph.fromFile(Path.of("data/small_cyclic.txt"));
        Metrics metrics = new Metrics();
        SCC.SCCResult result = SCC.kosaraju(graph, metrics);

        assertTrue(result.sccCount > 0, "SCC count must be positive");
    }

    @Test
    void testKahnCycleDetection() throws IOException {
        Graph cyclicGraph = Graph.fromFile(Path.of("data/small_cyclic.txt"));
        Metrics m1 = new Metrics();
        SCC.KahnResult kahnRes = SCC.kahn(cyclicGraph, m1);

        assertTrue(kahnRes.hasCycle, "Cyclic graph must be detected by Kahn's algorithm");
    }

    @Test
    void testStackSafetyLargePath() {
        // Проверка итеративного DFS на пути из 100 000 вершин (рекурсия вызвала бы StackOverflowError)
        int n = 100_000;
        Graph pathGraph = new Graph(n, true);
        for (int i = 0; i < n - 1; i++) {
            pathGraph.addEdge(i, i + 1, 1);
        }

        Metrics metrics = new Metrics();
        SCC.SCCResult result = SCC.kosaraju(pathGraph, metrics);

        assertEquals(n, result.sccCount, "Each vertex in a path is its own SCC");
        assertTrue(metrics.getMaxDepth() > 0, "Max depth should be recorded");
    }
}