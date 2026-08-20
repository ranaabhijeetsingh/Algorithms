# Algorithms

A practical repository for advanced algorithmic thinking and production-grade problem solving. The focus is on designing scalable, robust, and maintainable solutions for real-world graph challenges, especially in distributed systems, cybersecurity, fraud analysis, and network intelligence.

## Q1. How do you detect cycles and tightly connected subgraphs in a scale-free graph, and what is the computational complexity of evaluating multi-hop relationships?

### 1.1 Problem framing

This question has three important dimensions:

1. Detecting cyclic structures in a graph
2. Identifying dense or tightly connected subgraphs
3. Understanding the cost of exploring multi-hop relationships in large-scale networks

A scale-free graph is a network in which a small number of nodes have extremely high degree (hub-like behavior), while most nodes remain sparsely connected. Such graphs commonly occur in social networks, financial transaction networks, telecom graphs, and cyber threat maps.

### 1.2 Core concepts

- Cycle: a path that starts and ends at the same node, such as A -> B -> C -> A. In fraud detection, cycles may indicate circular transaction behavior or suspicious repeated movement of value.
- Tightly connected subgraph: a cluster where nodes are strongly interconnected. In security analysis, it can represent a coordinated fraud ring, botnet, or malicious campaign structure.
- Multi-hop relationship: a relationship that exists through intermediate nodes, such as friend-of-a-friend or transaction path of length 3 to 5.

### 1.3 Why this matters in production systems

In enterprise-grade systems, these patterns are often used in:

- Fraud and anti-money laundering analytics
- Cybersecurity and threat intelligence platforms
- Identity graph analysis
- Recommendation systems and social graph mining
- Network anomaly detection

The graph may contain millions of nodes and edges. In such environments, a naive traversal strategy can become computationally very expensive, especially when a hub node is encountered.

### 1.4 Detecting cycles

Cycle detection is typically performed with depth-first search (DFS) or a variant of graph traversal that tracks node states.

A node may be in one of three states:

- Unvisited
- Visited in the current DFS stack
- Fully processed

If a DFS encounters an edge to a node already in the current traversal stack, a cycle exists.

This approach is efficient and commonly used because it works in linear time relative to the number of vertices and edges in the graph.

### 1.5 Detecting tightly connected subgraphs

Tightly connected subgraphs are often detected using:

- Connected components for coarse grouping
- Strongly connected components (SCCs) in directed graphs
- Community detection algorithms for large-scale sparse graphs
- Clique or dense-subgraph detection for highly connected clusters

For directed transaction or attacker-path graphs, SCC detection is particularly valuable because it identifies subgraphs in which every node can reach every other node.

### 1.6 Complexity of multi-hop evaluation

Let:

- d = average degree of a node
- k = number of hops explored

Then the number of reachable nodes can grow approximately as O(d^k) in the worst case.

This means that exploring deeper paths is exponential in the hop count, and in scale-free graphs the effect is amplified because hub nodes create disproportionate branching factors. In practical systems, this is why graph queries must be bounded, pruned, indexed, or approximated.

In production pipelines, complexity is managed with:

- neighborhood sampling
- heuristic pruning
- graph indexing
- layered traversal constraints
- distributed graph processing frameworks

### 1.7 Java implementation example

Below is a production-oriented Java example that demonstrates DFS-based cycle detection for graph-like relationships such as circular transfers or alternate attack paths.

```java
import java.util.*;

public class FraudAndThreatDetector {
    private final Map<String, List<String>> adjacency;
    private final Set<String> visited = new HashSet<>();
    private final Set<String> currentPath = new HashSet<>();

    public FraudAndThreatDetector() {
        this.adjacency = new HashMap<>();
    }

    public void addEdge(String from, String to) {
        adjacency.computeIfAbsent(from, key -> new ArrayList<>()).add(to);
        adjacency.computeIfAbsent(to, key -> new ArrayList<>());
    }

    public boolean hasCycle() {
        for (String node : adjacency.keySet()) {
            if (visited.contains(node)) {
                continue;
            }
            if (dfs(node, null)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfs(String node, String parent) {
        visited.add(node);
        currentPath.add(node);

        for (String neighbor : adjacency.getOrDefault(node, Collections.emptyList())) {
            if (neighbor.equals(parent)) {
                continue;
            }

            if (currentPath.contains(neighbor)) {
                return true;
            }

            if (!visited.contains(neighbor) && dfs(neighbor, node)) {
                return true;
            }
        }

        currentPath.remove(node);
        return false;
    }

    public static void main(String[] args) {
        FraudAndThreatDetector detector = new FraudAndThreatDetector();

        detector.addEdge("A", "B");
        detector.addEdge("B", "C");
        detector.addEdge("C", "A");
        detector.addEdge("C", "D");

        System.out.println("Cycle detected: " + detector.hasCycle());
    }
}
```

### 1.8 Operational interpretation

This pattern is useful when analyzing:

- Circular transaction paths in financial crime investigations
- Lateral movement chains in enterprise security environments
- Repeated dependency loops in complex system architecture
- Hidden clusters in adversarial or anomalous network structures

The same logic becomes significantly more powerful when combined with graph databases, streaming event processing, and distributed graph analytics.

### 1.9 Industry-scale tooling

For production systems, engineering teams typically do not build graph logic entirely from scratch. Common tools include:

- JGraphT for Java-based graph algorithms
- Neo4j for graph database storage and traversal
- Apache TinkerPop for graph query abstractions
- Apache Kafka and Flink for streaming graph event processing
- Elasticsearch for search-oriented graph investigations and enriched analysis

These systems provide better scale, fault tolerance, and operational observability than a standalone in-memory implementation.

### 1.10 Summary

Detecting cycles and dense subgraphs in scale-free graphs is a fundamental challenge in large network analysis. The key idea is to combine graph traversal, state tracking, and domain-aware pruning. While DFS can efficiently detect cycles in O(V + E), multi-hop analysis can become exponentially expensive as path depth increases, especially in hub-heavy networks.

The real-world solution is to combine algorithmic correctness with scalable engineering: targeted traversal, efficient graph libraries, distributed processing, and domain-specific constraints.

---

This repository is intended to provide advanced algorithmic patterns and production-minded explanations for solving complex graph and optimization problems in real systems.
