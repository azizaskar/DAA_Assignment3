package algorithms;

import graph.Graph;
import metrics.Metrics;

import java.util.Arrays;
import java.util.List;

public class DAGShortestPath {

    public static class PathResult {
        public final long totalDistances;
        public final long[] dist;
        public final int[] parent;

        public PathResult(long totalDistances, long[] dist, int[] parent) {
            this.totalDistances = totalDistances;
            this.dist = dist;
            this.parent = parent;
        }
    }

    public static PathResult compute(Graph graph, Metrics metrics) {
        int n = graph.getVertexCount();

        // 1. Получаем топологический порядок (используем уже написанный алгоритм Кана)
        SCC.KahnResult kahnRes = SCC.kahn(graph, metrics);
        if (kahnRes.hasCycle) {
            throw new IllegalArgumentException("Graph has a cycle, cannot compute DAG shortest paths");
        }
        List<Integer> topOrder = kahnRes.order;

        // 2. Ищем вершины с нулевой входящей степенью (виртуальные супер-источники)
        int[] inDegree = new int[n];
        for (Graph.Edge edge : graph.getEdges()) {
            inDegree[edge.v]++;
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                dist[i] = 0;
            }
        }

        // 3. Релаксация ребер строго в топологическом порядке
        for (int u : topOrder) {
            if (dist[u] != Long.MAX_VALUE) {
                for (Graph.Edge edge : graph.getNeighbors(u)) {
                    metrics.incEdgeScans(); // По заданию релаксация DAG сканирует m ребер
                    int v = edge.v;
                    if (dist[u] + edge.weight < dist[v]) {
                        dist[v] = dist[u] + edge.weight;
                        parent[v] = u;
                    }
                }
            }
        }

        // 4. Считаем сумму всех расстояний (требуется для эталонной таблицы бенчмарка)
        long sumDist = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] != Long.MAX_VALUE) {
                sumDist += dist[i];
            }
        }

        return new PathResult(sumDist, dist, parent);
    }
}