import java.util.*;
public class FraudAndThreadDetector{

    // Graph representation using an Adjacency List: Account/IP -> List of connected Accounts/IPs
    private final Map<String, List<String>> adjList = new HashMap<>();

    public void addEdge(String source, String destination){
        adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(destination);
        adjList.putIfAbsent(destination, new ArrayList<>()); // ensure destination node exists
    }

    // Cycle detection entry point - initiates DFS from all unvisited nodes
    public List<List<String>> detectCycles(){
        Set<String> visited = new HashSet<>();
        Set<String> inStack = new HashSet<>();
        List<String> currentPath = new ArrayList<>();
        List<List<String>> detectedCycles = new ArrayList<>(); // Fixed: was detectedCycle

        for(String node : adjList.keySet()){
            if(!visited.contains(node)){
                dfsCycleSearch(node, visited, inStack, currentPath, detectedCycles); // Fixed: was detectCycles
            }
        }
        return detectedCycles; // Fixed: was detectCycles
    }

    // Depth-first search to detect cycles using visited set and recursion stack
    private void dfsCycleSearch(String node, Set<String> visited, Set<String> inStack, // Fixed: Stirng -> String
                               List<String> currentPath, List<List<String>> detectedCycles){
                                visited.add(node); // Mark node as visited
                                inStack.add(node); // Add node to current recursion stack
                                currentPath.add(node); // Add node to current path

                                // Check all neighbors of current node
                                for (String neighbor : adjList.getOrDefault(node, Collections.emptyList())){ // Fixed: string -> String, emptyLis -> emptyList
                                    if(inStack.contains(neighbor)){
                                        // Cycle Found! Extract the cycle segment from path
                                        int cycleStart = currentPath.indexOf(neighbor);
                                        List<String> cycle = new ArrayList<>(currentPath.subList(cycleStart, currentPath.size()));
                                        cycle.add(neighbor); // Complete the loop visually
                                        detectedCycles.add(cycle);
                                    }else if(!visited.contains(neighbor)){
                                        // Recursively search unvisited neighbors
                                        dfsCycleSearch(neighbor, visited, inStack, currentPath, detectedCycles);
                                    }
                                }

                                // Backtrack: remove node from recursion stack and current path
                                inStack.remove(node);
                                currentPath.remove(currentPath.size() - 1); // Fixed: space after -1

    }

    public static void main(String[] args){
        FraudAndThreadDetector detector = new FraudAndThreadDetector();

        // Fraud Pattern: Circular Transfer Ring(A -> B -> C -> A)
        detector.addEdge("Account_A", "Account_D");
        detector.addEdge("Account_B", "Account_C");
        detector.addEdge("Account_D", "Account_A");

        // Cyber Attack Pattern: Lateral Movement(Host 1 -> Host 2 -> Host 3)
        detector.addEdge("Host_101", "Host_102");

        List<List<String>> cycles = detector.detectCycles(); // Fixed: was detectCycles()
        System.out.println("----THREAD & FRAUD DETECTION REPORT -----"); // Fixed: DETE4CTION -> DETECTION
        for(List<String> cycle: cycles){
            System.out.println("ALERT - Suspicious Cycle Detected: " + String.join("->", cycle)); // Fixed: "->".cycle -> "->", cycle
        }

    }

}