package custom_data_structures;

import collection.KeyNotFoundException;
import collection.SaxGraph;
import collection.SaxList;

import java.util.Iterator;

public class MyGraph<V> implements SaxGraph<V> {
    private final MyHashMap<V, MyArrayList<DirectedEdge<V>>> adjacencyList;

    public MyGraph() {
        adjacencyList = new MyHashMap<>();
    }


    @Override
    public void addEdge(V fromValue, V toValue, double weight) throws KeyNotFoundException {
        if (!adjacencyList.contains(fromValue)) {
            adjacencyList.add(fromValue, new MyArrayList<>());
        }
        if (!adjacencyList.contains(toValue)) {
            adjacencyList.add(toValue, new MyArrayList<>());
        }
        adjacencyList.get(fromValue).addLast(new DirectedEdge<>(fromValue, toValue, weight));

    }



    @Override
    public void addEdgeBidirectional(V fromValue, V toValue, double weight) {
        addEdge(fromValue, toValue, weight);
        addEdge(toValue, fromValue, weight);
    }



    @Override
    public SaxList<DirectedEdge<V>> getEdges(V value) {
        if (!adjacencyList.contains(value)) {
            throw new KeyNotFoundException("" + value);
        }
        return adjacencyList.get(value);
    }


    @Override
    public double getTotalWeight() {
        double totalWeight = 0;

        for (V node : adjacencyList.getKeys()) {
            MyArrayList<DirectedEdge<V>> edges = adjacencyList.get(node);
            for (int i = 0; i < edges.size(); i++) {
                totalWeight += edges.get(i).weight();
            }
        }
        return totalWeight;
    }

    /**
     * Calculates the shortest paths from the specified start node to all other nodes
     * in the graph using Dijkstra's algorithm.
     * It produces a directed acyclic graph where each
     * node contains the shortest path from the start node.
     * This method uses a min-heap to prioritize nodes with the smallest accumulated weights.
     * It adds edges with minimum weights to the result graph until all reachable nodes are visited.
     *
     * @param startNode The node from which to calculate the shortest paths.
     * @return A new graph representing the shortest paths from the start node to all other reachable nodes.
     */

    @Override
    public SaxGraph<V> shortestPathsDijkstra(V startNode) {
        MyGraph<V> result = new MyGraph<>();
        MyHashMap<V, Boolean> visited = new MyHashMap<>();
        MyMinHeap<DirectedEdge<V>> queue = new MyMinHeap<>();
        queue.enqueue(new DirectedEdge<>(startNode, startNode, 0));
        while (!queue.isEmpty()) {
            DirectedEdge<V> currentEdge = queue.dequeue();

            if (visited.contains(currentEdge.from())) {
                continue;
            }

            visited.add(currentEdge.from(), true);
            if (!currentEdge.to().equals(currentEdge.from())) {

                result.addEdge(currentEdge.from(), currentEdge.to(), currentEdge.weight());

            }
            MyArrayList<DirectedEdge<V>> neighbors = adjacencyList.get(currentEdge.from());
            for (DirectedEdge<V> neighborEdge : neighbors) {
                V neighborVertex = neighborEdge.to();
                if (!visited.contains(neighborVertex)) {
                    queue.enqueue(new DirectedEdge<>(neighborVertex, currentEdge.from(), currentEdge.weight() + neighborEdge.weight()));
                }
            }
        }

        return result;
    }

    /**
     * This method performs backtracking to find the shortest path from the start node to the specified goal node
     * using the result of Dijkstra's algorithm.
     * This method is intended to be used after
     * `shortestPathsDijkstra` to retrieve the exact path.
     *
     * @param dijkstraGraph The output graph from `shortestPathsDijkstra`,
     *                      containing the shortest paths from the start node to all nodes in the original graph.
     * @param start         The starting node of the path.
     * @param goal          The goal node to which the path is traced back.
     * @return A list of directed edges representing the shortest path from the start node to the goal node.
     * @throws IllegalStateException If there is no path from the start node to the goal node in the Dijkstra result graph.
     */

    public SaxList<DirectedEdge<V>> backTrackDijkstra(SaxGraph<V> dijkstraGraph, V start, V goal) {
        MyArrayList<DirectedEdge<V>> path = new MyArrayList<>();
        V current = goal;


        while (!current.equals(start)) {
            SaxList<DirectedEdge<V>> edges;
            try {
                edges = dijkstraGraph.getEdges(current);
            } catch (KeyNotFoundException e) {
                throw new IllegalStateException("No path found to " + current);
            }


            DirectedEdge<V> edge = edges.get(0);

            path.addFirst(new DirectedEdge<>(edge.to(), edge.from(), edge.weight()));

            current = edge.to();
        }

        return path;
    }

    /**
     * Finds the shortest path from a start node to an end node in the graph using the A* algorithm.
     * <p>
     * The A* algorithm combines the actual cost from the start node (g) with an estimated cost to the end node (h)
     * to determine the total estimated cost (f) for each node.
     * Nodes with lower f-values are prioritized.
     * <p>
     * This method uses a MinHeap as a priority queue (`openList`) to keep track of nodes to be explored,
     * and a HashMap for closed list (`closedList`) to track nodes that have already been processed.
     * For each node, the algorithm evaluates its neighbors, updating the path if a shorter route is found.
     *
     * @param startNode The starting node of the search.
     * @param endNode   The goal node of the search.
     * @param estimator A heuristic function that estimates the cost from any node to the goal node.
     * @return A list of directed edges representing the shortest path from the start node to the end node,
     * or {@code null} if no path exists.
     */


    @Override
    public SaxList<DirectedEdge<V>> shortestPathAStar(V startNode, V endNode, Estimator<V> estimator) {
        MyMinHeap<AStarNode> openList = new MyMinHeap<>();
        MyHashMap<V, AStarNode> closedList = new MyHashMap<>();
        AStarNode startAStarNode = new AStarNode(null, startNode, 0, estimator.estimate(startNode, endNode), null);
        openList.enqueue(startAStarNode);
        while (!openList.isEmpty()) {

            AStarNode current = openList.dequeue();


            if (current.vertex.equals(endNode)) {


                return reconstructPath(current);
            }

            closedList.add(current.vertex, current);


            MyArrayList<DirectedEdge<V>> neighbors = adjacencyList.get(current.vertex);

            for (DirectedEdge<V> edge : neighbors) {
                V neighborNode = edge.to();

                if (closedList.contains(neighborNode)) {
                    continue;
                }

                double neighborG = current.g + edge.weight();
                double neighborH = estimator.estimate(neighborNode, endNode);
                AStarNode neighborAStarNode = new AStarNode(edge, neighborNode, neighborG, neighborH, current);

                openList.enqueue(neighborAStarNode);
            }
        }


        return null;
    }

    /**
     * Reconstructs the path from the goal node back to the start node using the chain of `previousVertex` references from each AStarNode.
     * <p>
     * This method is called after the A* algorithm has found the goal node.
     * It traces back through each node's
     * `previousVertex` to build the complete path.
     * The path is constructed in reverse order (from goal to start)
     * and is then returned in the correct order.
     *
     * @param goalNode The goal node reached by the A* algorithm, from which the path reconstruction begins.
     * @return A list of directed edges representing the path from the start node to the goal node.
     */

    private SaxList<DirectedEdge<V>> reconstructPath(AStarNode goalNode) {
        MyArrayList<DirectedEdge<V>> path = new MyArrayList<>();
        AStarNode current = goalNode;
        while (current.previousVertex != null) {

            path.addFirst(current.edgeToNode);
            current = current.previousVertex;
        }

        return path;
    }

    /**
     * The inner class AStarNode represents a node in the A* search algorithm,
     * storing information needed to find the shortest path.
     * All `AStarNode` stores:
     * </p>
     * - g: The actual cost from the start node to this node.
     * - h: The estimated cost from this node to the goal node, calculated with a heuristic function.
     * - f: The total estimated cost, where f = g + h. This value is used to prioritize nodes with lower `f` values.
     * </p>
     * The `AStarNode`
     * also keeps a reference to the previous node in the path (`previousVertex`) and the edge (`edgeToNode`)
     * that connects it.
     * These references allow the algorithm to reconstruct the path from the goal back to the start
     * once the shortest path is found.
     */

    private class AStarNode implements Comparable<AStarNode> {


        DirectedEdge<V> edgeToNode;  // The edge connecting this node to its predecessor in the path
        V vertex;     // The current vertex
        double g;     // The actual cost from the start node to this node
        double h;     // The heuristic cost from this node to the goal
        double f;     // The total cost f = g + h, used for priority in the A* search
        AStarNode previousVertex; // Reference to the previous node in the shortest path

        /**
         * Constructs an AStarNode with the specified parameters.
         *
         * @param edgeToNode     The edge connecting this node to its predecessor.
         * @param vertex         The vertex represented by this node.
         * @param g              The actual cost from the start node to this node.
         * @param h              The heuristic cost from this node to the goal node.
         * @param previousVertex The previous node in the path leading to this node.
         */

        AStarNode(DirectedEdge<V> edgeToNode, V vertex, double g, double h, AStarNode previousVertex) {
            this.edgeToNode = edgeToNode;
            this.vertex = vertex;
            this.g = g;
            this.h = h;
            this.f = g + h; // The f value is defined as the sum of the actual cost from the start node
            // (g) and the heuristic cost to the goal(h).
            this.previousVertex = previousVertex;
        }

        /**
         * Compares this AStarNode to another AStarNode based on their f-value.
         * This comparison is used by the priority queue in the A* algorithm
         * to prioritize nodes with the lowest f-value,
         * ensuring that nodes with the lowest estimated total cost are processed first.
         *
         * @param other The other AStarNode to compare to.
         * @return A negative integer, zero, or a positive integer if the f-value of this node
         * is less than, equal to, or greater than the specified node's f-value.
         */

        @Override
        public int compareTo(AStarNode other) {
            return Double.compare(this.f, other.f);
        }
    }

    /**
     * Computes the Minimum Cost Spanning Tree (MCST) of the graph using Prim's algorithm.
     * <p>
     * The algorithm begins with an arbitrary vertex (the first vertex in the graph)
     * and builds the Minimum Cost Spanning Tree by finding and adding the smallest edge
     * that connects a vertex  presented in a tree to a vertex outside the tree.
     * </p>
     * <p>
     * The MinHeap (priority queue) is used to quickly retrieve the smallest edge,
     * and a HashMap keeps track of which vertices have already been added to the tree.
     * </p>
     *
     * @return A new MyGraph instance representing the Minimum Cost Spanning Tree of the original graph.
     */
    @Override
    public SaxGraph<V> minimumCostSpanningTree() {
        // A new graph to store the Minimum Cost Spanning Tree (MCST)
        MyGraph<V> mst = new MyGraph<>();

        // HashMap to keep track of visited vertices to avoid cycles
        MyHashMap<V, Boolean> visited = new MyHashMap<>();

        // MinHeap (priority queue) for retrieval of the smallest edge
        MyMinHeap<DirectedEdge<V>> queue = new MyMinHeap<>();
        V startVertex = adjacencyList.getKeys().get(0);
        visited.add(startVertex, true);

        // Enqueue all edges of the starting vertex
        MyArrayList<DirectedEdge<V>> edgesOfStartVertex = adjacencyList.get(startVertex);
        for (DirectedEdge<V> edge : edgesOfStartVertex) {
            queue.enqueue(edge);
        }
        // Continue until there are no more edges to process
        while (!queue.isEmpty()) {
            // Dequeue the edge with the smallest weight
            DirectedEdge<V> currentEdge = queue.dequeue();

            V fromVertex = currentEdge.from();
            V toVertex = currentEdge.to();

            // Skip if the destination vertex is already visited to avoid cycles
            if (visited.contains(toVertex)) {
                continue;
            }
            // Mark the destination vertex as visited
            visited.add(toVertex, true);

            // Add the edge to the Minimum Cost Spanning Tree
            mst.addEdge(fromVertex, toVertex, currentEdge.weight());

            // Enqueue all edges of the newly added vertex that lead to unvisited vertices
            MyArrayList<DirectedEdge<V>> neighborsOfNextNode = adjacencyList.get(toVertex);
            for (DirectedEdge<V> neighborEdge : neighborsOfNextNode) {
                if (!visited.contains(neighborEdge.to())) {
                    queue.enqueue(neighborEdge);
                }
            }
        }


        return mst;
    }


    @Override
    public Iterator<V> iterator() {
        return new DFSIterator();
    }


    /**
     * This inner class represents the implementation of a Depth-First Search (DFS) iterator for graph traversal.
     * It uses a stack
     * to manage the nodes
     * to visit next and a hash map to keep track of visited nodes because nodes should be visited only once.
     */

    private class DFSIterator implements Iterator<V> {
        private final MyStack<V> stack = new MyStack<>();
        private final MyHashMap<V, Boolean> visited = new MyHashMap<>();


        /**
         * Initializes the DFS iterator. The traversal starts from the first node if the graph is not empty.
         */
        public DFSIterator() {
            if (!adjacencyList.isEmpty()) {
                V startNode = adjacencyList.getKeys().get(0);
                stack.push(startNode);
            }
        }

        /**
         * Checks if there are more nodes to visit in the DFS traversal, based on the stack state.
         *
         * @return true if there are more nodes to visit, false otherwise.
         */

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Retrieves the next unvisited vertex in the Depth-First Search (DFS) traversal.
         * This method removes the top vertex from the stack and checks if it has been visited.
         * If the vertex has not been visited yet, it is marked as visited,
         * and then its neighbors are added to the stack.
         * Only unvisited neighbors are added, ensuring they will be visited in future steps of the traversal.
         * The method continues to return nodes in DFS order until there are no more nodes left to visit.
         *
         * @return The next unvisited vertex in DFS order.
         * Returns {@code null} if there are no more unvisited nodes.
         */

        @Override
        public V next() {
            while (hasNext()) {
                V next = stack.pop();
                if (!visited.contains(next)) {
                    visited.add(next, true);

                    MyArrayList<DirectedEdge<V>> neighborsOfNext = adjacencyList.get(next);
                    for (DirectedEdge<V> edge : neighborsOfNext) {
                        V neighbor = edge.to();
                        if (!visited.contains(neighbor)) {
                            stack.push(neighbor);
                        }
                    }
                    return next;
                }
            }
            return null;
        }
    }



    @Override
    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }


    @Override
    public int size() {
        return adjacencyList.size();
    }


    /**
     * Generates a GraphViz DOT string representation of the graph.
     * Displays all edges with weights in a directed graph format.
     *
     * @param name The name of the graph.
     * @return A string in DOT format representing the graph.
     */

    @Override
    public String graphViz(String name) {
        StringBuilder builder = new StringBuilder();


        builder.append("digraph ").append(name).append(" {\n");
        MyHashMap<String, Boolean> processedEdges = new MyHashMap<>();
        for (V vertex : adjacencyList.getKeys()) {
            MyArrayList<DirectedEdge<V>> edges = adjacencyList.get(vertex);

            for (DirectedEdge<V> edge : edges) {
                String forwardEdge = "\"" + edge.from() + "\" -> \"" + edge.to() + "\"";
                String backwardEdge = "\"" + edge.to() + "\" -> \"" + edge.from() + "\"";


                if (!processedEdges.contains(forwardEdge) && !processedEdges.contains(backwardEdge)) {
                    builder.append(forwardEdge).append(" [label=\"").append(edge.weight()).append("\"]\n");
                    processedEdges.add(forwardEdge, true);


                    MyArrayList<DirectedEdge<V>> reverseEdges = adjacencyList.get(edge.to());
                    boolean reverseExists = false;
                    for (DirectedEdge<V> reverseEdge : reverseEdges) {
                        if (reverseEdge.to().equals(edge.from()) && reverseEdge.weight() == edge.weight()) {
                            reverseExists = true;
                            break;
                        }
                    }


                    if (reverseExists) {
                        builder.append(backwardEdge).append(" [label=\"").append(edge.weight()).append("\"]\n");
                        processedEdges.add(backwardEdge, true);
                    }
                }
            }


        }

        builder.append("}\n");
        return builder.toString();
    }
}
