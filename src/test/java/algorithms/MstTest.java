package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MstTest {
    @Test
    void testKruskalAndPrimAgreementOnConnectedGraph() throws IOException {
        Graph graph = Graph.fromFile(Path.of("data/small_connected.txt"));
        MST.MSTResult kruskalResult = MST.kruskal(graph, new Metrics());
        MST.MSTResult primResult = MST.prim(graph, new Metrics());
        assertEquals(kruskalResult.totalWeight, primResult.totalWeight);
    }

    @Test
    void testDisconnectedGraphForest() throws IOException {
        Graph graph = Graph.fromFile(Path.of("data/small_disconnected.txt"));
        MST.MSTResult kruskalResult = MST.kruskal(graph, new Metrics());
        MST.MSTResult primResult = MST.prim(graph, new Metrics());
        assertEquals(kruskalResult.totalWeight, primResult.totalWeight);
        assertTrue(kruskalResult.components > 1);
    }

    @Test
    void test100RandomConnectedGraphs() {
        Random rnd = new Random(42);
        int n = 30;
        for (int test = 0; test < 100; test++) {
            Graph g = new Graph(n, false);
            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int tmp = perm[i]; perm[i] = perm[j]; perm[j] = tmp;
            }
            for (int i = 1; i < n; i++) {
                g.addEdge(perm[rnd.nextInt(i)], perm[i], 1 + rnd.nextInt(100));
            }
            for (int i = 0; i < n; i++) {
                int u = rnd.nextInt(n), v = rnd.nextInt(n);
                if (u != v) g.addEdge(u, v, 1 + rnd.nextInt(100));
            }
            assertEquals(MST.kruskal(g, new Metrics()).totalWeight, MST.prim(g, new Metrics()).totalWeight);
        }
    }
}