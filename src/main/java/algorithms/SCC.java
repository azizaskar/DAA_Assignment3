package algorithms;

import graph.Graph;
import metrics.Metrics;

import java.util.*;

public class SCC {

    public static class SCCResult {
        public final int sccCount;
        public final List<List<Integer>> components;

        public SCCResult(int sccCount, List<List<Integer>> components) {
            this.sccCount = sccCount;
            this.components = components;
        }
    }

    public static class KahnResult {
        public final boolean hasCycle;
        public final List<Integer> order;

        public KahnResult(boolean hasCycle, List<Integer> order) {
            this.hasCycle = hasCycle;
            this.order = order;
        }
    }

    /**
     * Итеративный алгоритм Косараджу для поиска SCC, безопасный для стека (n = 100 000).
     */
    public static SCCResult kosaraju(Graph graph, Metrics metrics) {
        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        ArrayDeque<Integer> finishOrder = new ArrayDeque<>();

        // Первый проход: сбор порядка завершения
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                iterativeDfsFirstPass(graph, i, visited, finishOrder, metrics);
            }
        }

        // Второй проход на транспонированном графе
        Graph reversed = graph.reverse();
        Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();

        while (!finishOrder.isEmpty()) {
            int u = finishOrder.pop();
            if (!visited[u]) {
                List<Integer> component = new ArrayList<>();
                iterativeDfsSecondPass(reversed, u, visited, component, metrics);
                components.add(component);
            }
        }

        return new SCCResult(components.size(), components);
    }

    private static void iterativeDfsFirstPass(Graph graph, int start, boolean[] visited, ArrayDeque<Integer> finishOrder, Metrics metrics) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        ArrayDeque<Iterator<Graph.Edge>> iterStack = new ArrayDeque<>();

        stack.push(start);
        iterStack.push(graph.getNeighbors(start).iterator());
        visited[start] = true;

        metrics.updateMaxDepth(stack.size());

        while (!stack.isEmpty()) {
            int u = stack.peek();
            Iterator<Graph.Edge> it = iterStack.peek();

            metrics.updateMaxDepth(stack.size());

            if (it.hasNext()) {
                Graph.Edge edge = it.next();
                metrics.incEdgeScans();
                int v = edge.v;
                if (!visited[v]) {
                    visited[v] = true;
                    stack.push(v);
                    iterStack.push(graph.getNeighbors(v).iterator());
                }
            } else {
                stack.pop();
                iterStack.pop();
                finishOrder.push(u);
            }
        }
    }

    private static void iterativeDfsSecondPass(Graph reversed, int start, boolean[] visited, List<Integer> component, Metrics metrics) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        visited[start] = true;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            component.add(u);

            for (Graph.Edge edge : reversed.getNeighbors(u)) {
                metrics.incEdgeScans();
                int v = edge.v;
                if (!visited[v]) {
                    visited[v] = true;
                    stack.push(v);
                }
            }
        }
    }

    /**
     * Алгоритм Кана для топологической сортировки и обнаружения циклов.
     */
    public static KahnResult kahn(Graph graph, Metrics metrics) {
        int n = graph.getVertexCount();
        int[] inDegree = new int[n];

        for (Graph.Edge edge : graph.getEdges()) {
            metrics.incEdgeScans();
            inDegree[edge.v]++;
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                metrics.incEdgeScans();
                int v = edge.v;
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.add(v);
                }
            }
        }

        boolean hasCycle = (order.size() != n);
        return new KahnResult(hasCycle, order);
    }
}