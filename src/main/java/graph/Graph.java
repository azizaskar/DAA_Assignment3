package graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Graph {

    public static class Edge {
        public final int u;
        public final int v;
        public final int weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }

    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> edges;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.adj.add(new ArrayList<>());
        }
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int weight) {
        Edge forward = new Edge(u, v, weight);
        edges.add(forward);
        adj.get(u).add(forward);
        if (!directed && u != v) {
            adj.get(v).add(new Edge(v, u, weight));
        }
    }

    public int getVertexCount() { return n; }
    public int getEdgeCount() { return edges.size(); }
    public boolean isDirected() { return directed; }
    public List<Edge> getNeighbors(int u) { return adj.get(u); }
    public List<Edge> getEdges() { return edges; }

    public Graph reverse() {
        Graph reversed = new Graph(n, directed);
        for (Edge e : edges) {
            reversed.addEdge(e.v, e.u, e.weight);
        }
        return reversed;
    }

    public static Graph fromFile(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) throw new IOException("Empty graph file");

            StringTokenizer st = new StringTokenizer(header.trim());
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            boolean directed = "directed".equalsIgnoreCase(st.nextToken());

            Graph graph = new Graph(n, directed);
            for (int i = 0; i < m; i++) {
                String line = reader.readLine();
                if (line == null || line.trim().isEmpty()) {
                    i--;
                    continue;
                }
                StringTokenizer et = new StringTokenizer(line);
                int u = Integer.parseInt(et.nextToken());
                int v = Integer.parseInt(et.nextToken());
                int w = Integer.parseInt(et.nextToken());
                graph.addEdge(u, v, w);
            }
            return graph;
        }
    }
}