package algorithms;

import graph.Graph;
import metrics.Metrics;
import java.util.*;

public class MST {
    public static class MSTResult {
        public final long totalWeight;
        public final List<Graph.Edge> edges;
        public final int components;

        public MSTResult(long totalWeight, List<Graph.Edge> edges, int components) {
            this.totalWeight = totalWeight;
            this.edges = edges;
            this.components = components;
        }
    }

    public static MSTResult kruskal(Graph graph, Metrics metrics) {
        int n = graph.getVertexCount();
        DSU dsu = new DSU(n, metrics);
        List<Graph.Edge> sortedEdges = new ArrayList<>(graph.getEdges());

        sortedEdges.sort((e1, e2) -> {
            metrics.incComparisons();
            return Integer.compare(e1.weight, e2.weight);
        });

        long totalWeight = 0;
        List<Graph.Edge> mstEdges = new ArrayList<>();

        for (Graph.Edge edge : sortedEdges) {
            metrics.incEdgeScans();
            if (dsu.union(edge.u, edge.v)) {
                totalWeight += edge.weight;
                mstEdges.add(edge);
            }
        }
        return new MSTResult(totalWeight, mstEdges, dsu.components());
    }

    public static MSTResult prim(Graph graph, Metrics metrics) {
        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        List<Graph.Edge> mstEdges = new ArrayList<>();
        long totalWeight = 0;
        int components = 0;
        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        for (int start = 0; start < n; start++) {
            if (!visited[start]) {
                components++;
                visited[start] = true;
                for (Graph.Edge e : graph.getNeighbors(start)) {
                    metrics.incEdgeScans();
                    metrics.incComparisons();
                    pq.add(e);
                }
                while (!pq.isEmpty()) {
                    Graph.Edge edge = pq.poll();
                    int v = edge.v;
                    if (visited[v]) continue;
                    visited[v] = true;
                    totalWeight += edge.weight;
                    mstEdges.add(edge);
                    for (Graph.Edge nextEdge : graph.getNeighbors(v)) {
                        metrics.incEdgeScans();
                        metrics.incComparisons();
                        if (!visited[nextEdge.v]) pq.add(nextEdge);
                    }
                }
            }
        }
        return new MSTResult(totalWeight, mstEdges, components);
    }
}