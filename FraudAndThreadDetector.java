import java.util.*;
public class FraudAndThreadDetector{

    // Graph representation using an Adjecency List: Account/IP -> List of connected Accounts/IPs
    private final Map<String, List<String>> adjList = new HashMap<>();

    public void addEdge(String source, String destination){
        adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(destination);
        adjList.putIfAbsent(destination, new ArrayList<>()); // ensure destination node exists
    }

    // cycle detection entry Point
    public List<List<String>> detectCycle(){
        Set<String> visited = new HashSet<>();
        Set<String> inStack = new HashSet<>();
        List<String> currentPath = new ArrayList<>();
        List<List<String>> detectedCycle = new ArrayList<>();

        for(String node : adjList.keySet()){
            if(!visited.contains(node)){
                dfsCycleSearch(node, visited, inStack, currentPath, detectCycles);
            }
        }
        return detectCycles;
    }

    private void dfsCycleSearch(String node, Set<Stirng> inStack,
                               List<String> currentPath, List<List<String>> detectedCycles){
                                visited.add(node);
                                inStack.add(node);
                                currentPath.add(node);

                                for (string neighbor : adjList.getOrDefault(node, Collections.emptyLis())){
                                    if(inStack.contains(neighbor)){
                                        // Cycle Found! Extract the cycle segment from path
                                        int cycleStart = currentPath.indexOf(neighbor);
                                        List<String> cycle = new ArrayList<>(currentPath.subList(cycleStart, currentPath.size()));
                                        cycle.add(neighbor); // Complete the loop visually
                                        detectedCycles.add(cycle);
                                    }else if(!visited.contains(neighbor)){
                                        dfsCycleSearch(neighbor, visited, inStack, currentPath, detectedCycles);
                                    }
                                }

                                // BackTrack
                                inStack.remove(node);
                                currentPath.remove(currentPath.size() -1);

    }

    public static void main(String[] args){
        FraudAndThreadDetector detector = new FraudAndThreadDetector();

        // Fraud Pattern: Circular Transfer Ring(A -> B -> C -> A)
        detector.addEdge("Account_A", "Account_B");
        detector.addEdge("Account_B", "Account_C");
        detector.addEdge("Account_C", "Account_A");

        // Cyber Arract Pattern: Lateral Movement(Host 1 -> Host 2 -> Host 3)
        detector.addEdge("Host_101", "Host_102");

        List<List<String>> cycles = detector.detectCycles();
        System.out.println("----THREAD & FRAUD DETE4CTION REPORT -----");
        for(List<String> cycle: cycles){
            System.out.println("ALERT - Suspicious Cycle Detected: " + String.join("->".cycle));
        }

    }

}