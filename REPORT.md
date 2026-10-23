# Assignment 3 Report

## Complexity Table
| Algorithm | Time | Extra Space | Justification |
| :--- | :--- | :--- | :--- |
| DSU | O(alpha(n)) | O(V) | Path compression and union by rank ensure near-constant time. |
| Kruskal | O(E log E) | O(V+E) | Bottleneck is sorting edges. Space needed for edge list and DSU. |
| Prim | O(E log V) | O(V) | Priority queue operations take log V. |
| Kosaraju | O(V+E) | O(V) | Two iterative DFS passes. Explicit stack takes O(V). |
| Kahn | O(V+E) | O(V) | Queue-based traversal processes each vertex and edge once. |
| DAG Shortest Path| O(V+E) | O(V) | Relaxes edges exactly once in topological order. |

## Correctness & Metrics
Our metrics empirically confirm the complexity classes (e.g., Kahn edgeScans / m is exactly 1.00). 
* **MST Cut Property**: The lightest edge crossing a cut is always safe; Kruskal and Prim exploit this globally and locally.
* **DAG Path Induction**: Relaxing edges in topological order guarantees all prefix paths are resolved before a vertex is processed.
