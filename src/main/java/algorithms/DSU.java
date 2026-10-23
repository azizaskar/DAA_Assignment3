package algorithms;

import metrics.Metrics;

public class DSU {
    private int[] parent;
    private int[] rank;
    private int components;
    private final Metrics metrics;

    public DSU(int n, Metrics metrics) {
        this.metrics = metrics != null ? metrics : new Metrics();
        makeSet(n);
    }

    public final void makeSet(int n) {
        this.parent = new int[n];
        this.rank = new int[n];
        this.components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        metrics.incFind();
        int root = x;
        while (root != parent[root]) {
            root = parent[root];
        }
        int curr = x;
        while (curr != root) {
            int nxt = parent[curr];
            parent[curr] = root;
            curr = nxt;
        }
        return root;
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return false;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        components--;
        metrics.incUnion();
        return true;
    }

    public int components() { return components; }
    public Metrics getMetrics() { return metrics; }
}