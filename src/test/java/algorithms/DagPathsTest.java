package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DagPathsTest {

    @Test
    void testDagShortestPathVsBellmanFordWithNegativeWeights() {
        int n = 6;
        Graph dag = new Graph(n, true);

        // Создаем DAG с отрицательными весами (но без отрицательных циклов, т.к. это DAG)
        dag.addEdge(0, 1, 5);
        dag.addEdge(0, 2, 3);
        dag.addEdge(1, 3, 6);
        dag.addEdge(1, 2, 2);
        dag.addEdge(2, 4, 4);
        dag.addEdge(2, 5, 2);
        dag.addEdge(2, 3, 7);
        dag.addEdge(3, 4, -1); // Отрицательный вес!
        dag.addEdge(4, 5, -2); // Отрицательный вес!

        DAGShortestPath.PathResult dagResult = DAGShortestPath.compute(dag, new Metrics());

        // Наивный алгоритм Беллмана-Форда для проверки
        long[] bfDist = new long[n];
        Arrays.fill(bfDist, Long.MAX_VALUE);
        int[] inDegree = new int[n];
        for (Graph.Edge e : dag.getEdges()) inDegree[e.v]++;
        for (int i = 0; i < n; i++) if (inDegree[i] == 0) bfDist[i] = 0;

        for (int i = 0; i < n - 1; i++) {
            for (Graph.Edge e : dag.getEdges()) {
                if (bfDist[e.u] != Long.MAX_VALUE && bfDist[e.u] + e.weight < bfDist[e.v]) {
                    bfDist[e.v] = bfDist[e.u] + e.weight;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            assertEquals(bfDist[i], dagResult.dist[i], "Mismatch at vertex " + i);
        }
    }
}