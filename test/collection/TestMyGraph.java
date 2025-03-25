package collection;

import custom_data_structures.MyArrayList;
import custom_data_structures.MyGraph;
import custom_data_structures.MyHashMap;
import app.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyGraph {
    private MyGraph<Integer> myGraph;
    private MyGraph<String> graph;


    @BeforeEach
    public void setUpMyGraph() {
        this.myGraph = new MyGraph<>();
        this.graph = new MyGraph<>();
    }

    public void setUpBasicGraph() {
        myGraph.addEdge(1, 2, 1);
        myGraph.addEdge(2, 3, 2);
        myGraph.addEdge(3, 4, 3);
    }

    public void setUpGraphForDFSTraversal() {
        myGraph.addEdge(1, 2, 1);
        myGraph.addEdge(2, 4, 2);
        myGraph.addEdge(1, 3, 2);
        myGraph.addEdge(3, 6, 3);
    }

    public void setUpGraphForDijkstra() {
        graph.addEdge("a", "b", 1);
        graph.addEdge("a", "c", 4);
        graph.addEdge("b", "e", 10);
        graph.addEdge("b", "d", 3);
        graph.addEdge("c", "d", 2);
        graph.addEdge("c", "g", 3);
        graph.addEdge("d", "e", 5);
        graph.addEdge("d", "f", 7);
        graph.addEdge("d", "g", 1);
        graph.addEdge("e", "f", 7);
        graph.addEdge("g", "f", 5);

    }

    public void setUpPrimGraph() {
        graph.addEdgeBidirectional("a", "b", 4);
        graph.addEdgeBidirectional("a", "h", 8);
        graph.addEdgeBidirectional("b", "h", 11);
        graph.addEdgeBidirectional("b", "c", 8);
        graph.addEdgeBidirectional("c", "i", 2);
        graph.addEdgeBidirectional("c", "f", 4);
        graph.addEdgeBidirectional("c", "d", 7);
        graph.addEdgeBidirectional("d", "f", 14);
        graph.addEdgeBidirectional("d", "e", 9);
        graph.addEdgeBidirectional("e", "f", 10);
        graph.addEdgeBidirectional("f", "g", 2);
        graph.addEdgeBidirectional("g", "i", 6);
        graph.addEdgeBidirectional("g", "h", 1);
        graph.addEdgeBidirectional("h", "i", 7);
    }


    @Test
    public void GivenEmptyGraph_WhenCheckIfIsEmpty_ThenTheTrueValueReturned() {
        assertTrue(myGraph.isEmpty());
    }

    @Test
    public void GivenNonEmptyGraph_WhenCheckIfIsEmpty_ThenTheFalseValueReturned() {
        setUpBasicGraph();
        assertFalse(myGraph.isEmpty());
    }

    @Test
    public void GivenNonEmptyGraph_WhenGetEdges_ThenTheEdgesSetUpAndReturnedCorrectly() {
        setUpBasicGraph();
        assertFalse(myGraph.isEmpty());
        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex1 = myGraph.getEdges(1);
        assertEquals(1, edgesForVertex1.size());
        assertEquals(2, edgesForVertex1.get(0).to());
        assertEquals(1, edgesForVertex1.get(0).from());


        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex2 = myGraph.getEdges(2);
        assertEquals(1, edgesForVertex2.size());
        assertEquals(3, edgesForVertex2.get(0).to());
        assertEquals(2, edgesForVertex2.get(0).from());


        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex3 = myGraph.getEdges(3);
        assertEquals(1, edgesForVertex3.size());
        assertEquals(4, edgesForVertex3.get(0).to());
        assertEquals(3, edgesForVertex3.get(0).from());

    }

    @Test
    public void GivenNonEmptyGraph_WhenGetEdgesOfNonExistentVertex_ThenTheKeyNotFoundExceptionIsThrown() {
        setUpBasicGraph();
        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> myGraph.getEdges(0));
        assertEquals("Key \"" + 0 + "\" is not found.", exception.getMessage());
    }

    @Test
    public void GivenGraph_WhenGetTotalWeightOfEdges_ThenTheCorrectTotalWeightReturned() {
        setUpBasicGraph();
        assertEquals(6, myGraph.getTotalWeight());
    }

    @Test
    public void GivenGraph_WhenGetSize_ThenTheCorrectSizeIsReturned() {
        setUpBasicGraph();
        assertEquals(4, myGraph.size());

    }

    @Test
    public void GivenGraph_WhenAddBidirectionalEdge_ThenTheCorrespondingVertexesContainsEdges() {
        myGraph.addEdgeBidirectional(1, 2, 1);
        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex1 = myGraph.getEdges(1);
        assertEquals(1, edgesForVertex1.size());
        assertEquals(2, edgesForVertex1.get(0).to());
        assertEquals(1, edgesForVertex1.get(0).from());


        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex2 = myGraph.getEdges(2);
        assertEquals(1, edgesForVertex2.size());
        assertEquals(1, edgesForVertex2.get(0).to());
        assertEquals(2, edgesForVertex2.get(0).from());

    }

    @Test
    public void GivenGraph_WhenIterateThroughGraphWithDFS_ThenTheIteratorReturnsTheVertexesInDFSOrder() {
        setUpGraphForDFSTraversal();

        MyArrayList<Integer> traversalOrder = new MyArrayList<>();

        for (Integer integer : myGraph) {
            traversalOrder.addLast(integer);
        }
        assertEquals(1, traversalOrder.get(0));
        assertEquals(3, traversalOrder.get(1));
        assertEquals(6, traversalOrder.get(2));
        assertEquals(2, traversalOrder.get(3));
        assertEquals(4, traversalOrder.get(4));


    }

    @Test
    public void GivenEmptyGraph_WhenIterateThroughGraphWithDFS_ThenTheIteratorReturnsNull() {
        Iterator<Integer> dfsIterator = myGraph.iterator();
        assertFalse(dfsIterator.hasNext());
        assertNull(dfsIterator.next());
    }

    @Test
    public void GivenConnectedGraph_WhenUsingPrimAlgorithmForMinimumCostSpanningTree_ThenTheCorrectMinimumSpanningTreeIsReturned() {
        myGraph.addEdgeBidirectional(1, 2, 1);
        myGraph.addEdgeBidirectional(2, 3, 2);
        myGraph.addEdgeBidirectional(3, 4, 3);
        myGraph.addEdgeBidirectional(4, 1, 4);
        myGraph.addEdgeBidirectional(1, 3, 5);

        MyGraph<Integer> minimumCostSpanningTree = (MyGraph<Integer>) myGraph.minimumCostSpanningTree();
        assertEquals(6, minimumCostSpanningTree.getTotalWeight());

        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex1 = minimumCostSpanningTree.getEdges(1);
        assertEquals(1, edgesForVertex1.size());
        assertEquals(2, edgesForVertex1.get(0).to());
        assertEquals(1, edgesForVertex1.get(0).weight());

        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex2 = minimumCostSpanningTree.getEdges(2);
        assertEquals(1, edgesForVertex2.size());
        assertEquals(3, edgesForVertex2.get(0).to());
        assertEquals(2, edgesForVertex2.get(0).weight());


        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex3 = minimumCostSpanningTree.getEdges(3);
        assertEquals(1, edgesForVertex3.size());
        assertEquals(4, edgesForVertex3.get(0).to());
        assertEquals(3, edgesForVertex3.get(0).weight());

        assertEquals(4, minimumCostSpanningTree.size());
        int mstEdgeCount = edgesForVertex1.size() + edgesForVertex2.size() + edgesForVertex3.size();
        assertEquals(3, mstEdgeCount);


    }

    @Test
    public void GivenDisconnectedGraph_WhenUsingPrimAlgorithmForMinimumCostSpanningTree_ThenTreeContainsOnlyConnectedComponents() {

        myGraph.addEdgeBidirectional(1, 2, 1);
        myGraph.addEdgeBidirectional(2, 3, 2);
        myGraph.addEdgeBidirectional(3, 4, 3);


        myGraph.addEdgeBidirectional(5, 6, 1);
        myGraph.addEdgeBidirectional(6, 7, 2);


        MyGraph<Integer> minimumCostSpanningTree = (MyGraph<Integer>) myGraph.minimumCostSpanningTree();


        assertEquals(6, minimumCostSpanningTree.getTotalWeight());


        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex1 = minimumCostSpanningTree.getEdges(1);
        assertEquals(1, edgesForVertex1.size());
        assertEquals(2, edgesForVertex1.get(0).to());
        assertEquals(1, edgesForVertex1.get(0).weight());

        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex2 = minimumCostSpanningTree.getEdges(2);
        assertEquals(1, edgesForVertex2.size());
        assertEquals(3, edgesForVertex2.get(0).to());
        assertEquals(2, edgesForVertex2.get(0).weight());

        SaxList<SaxGraph.DirectedEdge<Integer>> edgesForVertex3 = minimumCostSpanningTree.getEdges(3);
        assertEquals(1, edgesForVertex3.size());
        assertEquals(4, edgesForVertex3.get(0).to());
        assertEquals(3, edgesForVertex3.get(0).weight());


        assertThrows(KeyNotFoundException.class, () -> minimumCostSpanningTree.getEdges(5));
        assertThrows(KeyNotFoundException.class, () -> minimumCostSpanningTree.getEdges(6));
        assertThrows(KeyNotFoundException.class, () -> minimumCostSpanningTree.getEdges(7));


        int mstEdgeCount = edgesForVertex1.size() + edgesForVertex2.size() + edgesForVertex3.size();
        assertEquals(3, mstEdgeCount);


        assertEquals(4, minimumCostSpanningTree.size());
    }

    @Test
    public void GivenGraph_WhenUsingAStarAlgorithm_ThenTheShortestPathIsReturned() {
        MyGraph<Coordinate> myCoordinateGraph = new MyGraph<>();
        Coordinate start = new Coordinate(52.3676, 4.9041);// Amsterdam
        Coordinate middle1 = new Coordinate(52.2297, 5.1669); // Almere
        Coordinate middle2 = new Coordinate(51.4416, 5.4697); // Eindhoven
        Coordinate goal = new Coordinate(51.9225, 4.47917); // Rotterdam

        myCoordinateGraph.addEdge(start, middle1, 1.3);
        myCoordinateGraph.addEdge(middle1, middle2, 2.7);
        myCoordinateGraph.addEdge(middle2, goal, 2.3);
        myCoordinateGraph.addEdge(start, goal, 100.0);

        SaxGraph.Estimator<Coordinate> estimator = Coordinate::haversineDistance;
        SaxList<SaxGraph.DirectedEdge<Coordinate>> shortestPathAStar = myCoordinateGraph.shortestPathAStar(start, goal, estimator);

        assertNotNull(shortestPathAStar);
        assertEquals(3, shortestPathAStar.size());
        double totalPathWeight = 0;


        for (int i = 0; i < shortestPathAStar.size(); i++) {
            SaxGraph.DirectedEdge<Coordinate> edge = shortestPathAStar.get(i);

            totalPathWeight += edge.weight();
        }

        assertEquals(6.3, totalPathWeight);

    }


    @Test
    public void GivenGraphWithUnreachableGoal_WhenUsingAStarAlgorithm_ThenNoPathIsReturned() {
        MyGraph<Coordinate> myCoordinateGraph = new MyGraph<>();
        Coordinate start = new Coordinate(52.3676, 4.9041); // Amsterdam
        Coordinate middle = new Coordinate(52.2297, 5.1669); // Almere
        Coordinate end = new Coordinate(51.4416, 5.4697); // Eindhoven
        Coordinate goal = new Coordinate(51.9225, 4.47917); // Rotterdam (unreachable from start)


        myCoordinateGraph.addEdge(start, middle, 1.3);
        myCoordinateGraph.addEdge(middle, end, 2.7);


        SaxGraph.Estimator<Coordinate> estimator = Coordinate::haversineDistance;
        SaxList<SaxGraph.DirectedEdge<Coordinate>> shortestPathAStar = myCoordinateGraph.shortestPathAStar(start, goal, estimator);


        assertNull(shortestPathAStar);
    }

    @Test
    public void GivenGraph_WhenUsingDijkstraAlgorithm_ThenTheShortestPathFromAVertexToFVertexIsCorrect() {
        setUpGraphForDijkstra();

        String startVertex = "a";
        String goal = "f";

        SaxGraph<String> dijkstraGraph = graph.shortestPathsDijkstra(startVertex);
        SaxList<SaxGraph.DirectedEdge<String>> pathToF = graph.backTrackDijkstra(dijkstraGraph, startVertex, goal);
        assertNotNull(pathToF);
        assertEquals(4, pathToF.size());
        double totalWeight = pathToF.get(pathToF.size() - 1).weight();


        assertEquals(10.0, totalWeight, 0.1);

        assertEquals("a", pathToF.get(0).from());
        assertEquals("b", pathToF.get(0).to());

        assertEquals("b", pathToF.get(1).from());
        assertEquals("d", pathToF.get(1).to());

        assertEquals("d", pathToF.get(2).from());
        assertEquals("g", pathToF.get(2).to());

        assertEquals("g", pathToF.get(3).from());
        assertEquals("f", pathToF.get(3).to());


    }


    @Test
    public void GivenGraphWithUnconnectedVertexes_WhenUsingDijkstraAlgorithm_ThenTheIllegalStateExceptionIsThrown() {
        setUpGraphForDijkstra();

        graph.addEdge("x", "y", 1.0);
        graph.addEdge("y", "z", 1.0);

        String startVertex = "a";
        String goal = "z";
        SaxGraph<String> dijkstraGraph = graph.shortestPathsDijkstra(startVertex);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> graph.backTrackDijkstra(dijkstraGraph, startVertex, goal));
        assertEquals("No path found to " + goal, exception.getMessage());

    }


    @Test
    public void GivenGraph_WhenUsingPrimAlgorithmForMCST_ThenTheMCSTSetUpCorrectly() {
        setUpPrimGraph();

        SaxGraph<String> mst = graph.minimumCostSpanningTree();

        assertEquals(37, mst.getTotalWeight());
        assertEquals(9, mst.size());


        MyArrayList<SaxGraph.DirectedEdge<String>> expectedEdges = new MyArrayList<>();

        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("a", "b", 4));
        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("a", "h", 8));
        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("h", "g", 1));
        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("g", "f", 2));
        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("f", "c", 4));
        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("c", "i", 2));
        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("c", "d", 7));
        expectedEdges.addLast(new SaxGraph.DirectedEdge<>("d", "e", 9));


        MyHashMap<String, Boolean> visitedEdges = new MyHashMap<>();
        MyArrayList<SaxGraph.DirectedEdge<String>> actualEdges = new MyArrayList<>();

        for (String node : mst) {
            SaxList<SaxGraph.DirectedEdge<String>> edges = mst.getEdges(node);
            for (SaxGraph.DirectedEdge<String> edge : edges) {
                String directEdge = edge.from() + "-" + edge.to();
                String reverseEdge = edge.to() + "-" + edge.from();
                if (!visitedEdges.contains(directEdge) && !visitedEdges.contains(reverseEdge)) {
                    actualEdges.addLast(edge);
                    visitedEdges.add(directEdge, true);
                    visitedEdges.add(reverseEdge, true);
                }
            }
        }


        assertEquals(expectedEdges.size(), actualEdges.size());

        for (SaxGraph.DirectedEdge<String> expectedEdge : expectedEdges) {
            boolean matchFound = false;
            for (int i = 0; i < actualEdges.size(); i++) {
                SaxGraph.DirectedEdge<String> actualEdge = actualEdges.get(i);
                if ((expectedEdge.from().equals(actualEdge.from()) && expectedEdge.to().equals(actualEdge.to()) && expectedEdge.weight() == actualEdge.weight()) ||
                        (expectedEdge.from().equals(actualEdge.to()) && expectedEdge.to().equals(actualEdge.from()) && expectedEdge.weight() == actualEdge.weight())) {
                    matchFound = true;
                    break;
                }
            }
            assertTrue(matchFound);
        }


    }


    @Test
    public void GivenNonEmptyOneDirectedGraph_WhenUseGraphViz_ThenTheCorrectGraphIsReturned() {
        setUpGraphForDijkstra();
        String expectedGraph = """
                digraph MyGraph {
                "a" -> "b" [label="1.0"]
                "a" -> "c" [label="4.0"]
                "b" -> "e" [label="10.0"]
                "b" -> "d" [label="3.0"]
                "c" -> "d" [label="2.0"]
                "c" -> "g" [label="3.0"]
                "d" -> "e" [label="5.0"]
                "d" -> "f" [label="7.0"]
                "d" -> "g" [label="1.0"]
                "e" -> "f" [label="7.0"]
                "g" -> "f" [label="5.0"]
                }""";


        String actualDotGraph = graph.graphViz().trim();
       assertEquals(expectedGraph.trim(), actualDotGraph);
    }

    @Test
    public void GivenNonEmptyBidDirectionalGraph_WhenUseGraphViz_ThenTheCorrectGraphIsReturned() {
        graph.addEdgeBidirectional("a", "b", 1);
        graph.addEdgeBidirectional("a", "c", 4);
        graph.addEdgeBidirectional("b", "e", 10);
        graph.addEdgeBidirectional("b", "d", 3);
        graph.addEdgeBidirectional("c", "d", 2);
        graph.addEdgeBidirectional("c", "g", 3);
        graph.addEdgeBidirectional("d", "e", 5);
        graph.addEdgeBidirectional("d", "f", 7);
        graph.addEdgeBidirectional("d", "g", 1);
        graph.addEdgeBidirectional("e", "f", 7);
        graph.addEdgeBidirectional("g", "f", 5);

        String expectedDotGraph= """
            digraph MyGraph {
            "a" -> "b" [label="1.0"]
            "b" -> "a" [label="1.0"]
            "a" -> "c" [label="4.0"]
            "c" -> "a" [label="4.0"]
            "b" -> "e" [label="10.0"]
            "e" -> "b" [label="10.0"]
            "b" -> "d" [label="3.0"]
            "d" -> "b" [label="3.0"]
            "c" -> "d" [label="2.0"]
            "d" -> "c" [label="2.0"]
            "c" -> "g" [label="3.0"]
            "g" -> "c" [label="3.0"]
            "d" -> "e" [label="5.0"]
            "e" -> "d" [label="5.0"]
            "d" -> "f" [label="7.0"]
            "f" -> "d" [label="7.0"]
            "d" -> "g" [label="1.0"]
            "g" -> "d" [label="1.0"]
            "e" -> "f" [label="7.0"]
            "f" -> "e" [label="7.0"]
            "f" -> "g" [label="5.0"]
            "g" -> "f" [label="5.0"]
            }
            """;

        String actualDotGraph = graph.graphViz().trim();
        assertEquals(expectedDotGraph.trim(), actualDotGraph);


    }


    @Test
    public void GivenEmptyGraph_WhenUseGraphViz_TheTheEmptyGraphIsReturned() {
        String actualDotGraph = graph.graphViz().trim();
        String expectedDotGraph ="digraph MyGraph {\n" +
                "}";
       assertEquals(expectedDotGraph, actualDotGraph);
    }


}





