package nl.saxion.cds.solution;

import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.custom_data_structures.MyAVLBinarySearchTree;
import nl.saxion.cds.custom_data_structures.MyArrayList;
import nl.saxion.cds.custom_data_structures.MyGraph;
import nl.saxion.cds.custom_data_structures.MyHashMap;
import nl.saxion.cds.solution.model.*;
import nl.saxion.cds.utils.reader.Creator;
import nl.saxion.cds.utils.reader.LambdaReader;

import java.io.IOException;
import java.util.Comparator;

public class TrackManager {

    /**
     * The TrackManager class manages a network of railway tracks and stations.
     * It provides functionality to load stations and tracks data from files,
     * create graphs for the rail network, and retrieve information about
     * stations, tracks, and paths using Dijkstra's algorithm for shortest paths
     * and Prim's algorithm for minimum cost spanning trees.
     */

    private MyArrayList<Station> stations;
    private MyArrayList<Track> tracks;
    private final MyHashMap<String, Station> stationMap = new MyHashMap<>();
    private final MyHashMap<String, String> stationNameToCodeMap = new MyHashMap<>();
    private final MyAVLBinarySearchTree<String, Station> stationsTree = new MyAVLBinarySearchTree<>();
    private final MyGraph<String> railNetworkGraph = new MyGraph<>();
    private final MyGraph<String> nlRailNetworkGraph = new MyGraph<>();


    public TrackManager() {
        try {
            loadStations();
            loadTracks();
            loadGraph();
            loadNetherlandsGraph();
            for (Station station : stations) {
                stationMap.add(station.getCode(), station);
                stationNameToCodeMap.add(station.getName().toLowerCase(), station.getCode());
                stationsTree.add(station.getName().toLowerCase(), station);

            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }


    }

    /**
     * Displays the minimum cost spanning tree (MCST) for stations in the Netherlands
     * by generating and printing the tree details, including total distance and connections.
     */

    public void displayMCSTForNetherlandsStations() {
        SaxGraph<String> mst= nlRailNetworkGraph.minimumCostSpanningTree();
        showMCST(mst);
    }


    /**
     * Displays the minimum cost spanning tree (MCST) for all stations in the rail network,
     * printing details of the tree including total length and connections.
     */

    public void displayMCSTForAllStations() {
        SaxGraph<String> mst= railNetworkGraph.minimumCostSpanningTree();
        showMCST(mst);
    }


    /**
     * Helper method to display the details of a minimum cost spanning tree (MCST),
     * including the number of connections between stations and the total length of the tree.
     * @param graph The graph for which the MCST will be displayed.
     */

    public void showMCST(SaxGraph<String> graph) {
      SaxGraph<String> mst= graph.minimumCostSpanningTree();
        double totalLength = 0.0;
        int connectionCount = 0;

        MyHashMap<String, Boolean> visitedEdges = new MyHashMap<>();

        for (String node : mst) {
            for (SaxGraph.DirectedEdge<String> edge : mst.getEdges(node)) {
                String fromCode = edge.from();
                String toCode = edge.to();
                String directEdge = fromCode + "-" + toCode;
                String reverseEdge = toCode + "-" + fromCode;


                if (!visitedEdges.contains(directEdge) && !visitedEdges.contains(reverseEdge)) {
                    String fromStationName = findStationByCode(fromCode).getName();
                    String toStationName = findStationByCode(toCode).getName();


                    System.out.println(fromStationName + " -> " + toStationName);


                    connectionCount++;
                    totalLength += edge.weight();


                    visitedEdges.add(directEdge, true);
                    visitedEdges.add(reverseEdge, true);
                }
            }
        }


        System.out.println("Total number of connections: " + connectionCount);
        System.out.println("Total length: " + String.format("%.1f", totalLength) + " km");



    }

    /**
     * Loads data for all stations located in the Netherlands into a separate graph,
     * and ensures
     * that only bidirectional or one-way connections within the country are included without duplicates.
     */

    public void loadNetherlandsGraph() {
        MyArrayList<Station> nlStations = filterOnlyNetherlandsStations();


        MyHashMap<String, Boolean> nlStationCodes = new MyHashMap<>();
        for (Station station : nlStations) {
            nlStationCodes.add(station.getCode(), true);
        }
        MyHashMap<String, Boolean> addedEdges = new MyHashMap<>();


        for (Track track : tracks) {
            String fromCode = track.getFrom();
            String toCode = track.getTo();
            String directEdge = fromCode + "-" + toCode;
            String reverseEdge = toCode + "-" + fromCode;


            if (nlStationCodes.contains(fromCode) && nlStationCodes.contains(toCode)) {
                if (!addedEdges.contains(directEdge) && !addedEdges.contains(reverseEdge)) {

                if (isBidirectionalTrack(track)) {
                    nlRailNetworkGraph.addEdgeBidirectional(fromCode, toCode, track.getDistanceInKilometers());
                } else {
                    nlRailNetworkGraph.addEdge(fromCode, toCode, track.getDistanceInKilometers());
                }

                    addedEdges.add(directEdge, true);
                    addedEdges.add(reverseEdge, true);
            }
            }
        }
    }


    /**
     * Loads the entire rail network into a graph structure, handling bidirectional and
     * one-way tracks.Ensures that duplicate tracks are not added.
     */

    public void loadGraph() {
        MyHashMap<String, Boolean> addedEdges = new MyHashMap<>();
        for (Track track : tracks) {

            String directEdge = track.getFrom() + "-" + track.getTo();
            String reverseEdge = track.getTo() + "-" + track.getFrom();

            if (!addedEdges.contains(directEdge) && !addedEdges.contains(reverseEdge)) {



                if (isBidirectionalTrack(track)) {
                    railNetworkGraph.addEdgeBidirectional(track.getFrom(), track.getTo(), track.getDistanceInKilometers());
                } else {
                    railNetworkGraph.addEdge(track.getFrom(), track.getTo(), track.getDistanceInKilometers());
                }


                addedEdges.add(directEdge, true);
                addedEdges.add(reverseEdge, true);
            }
        }

    }





    /**
     * Loads station data from a CSV file using a LambdaReader.
     * Each entry in the file is created with use of the Creator interface.
     *
     * @throws IOException If an error occurs during file reading.
     */

    public void loadStations() throws IOException {
        Creator<Station, MyArrayList<String>> stationCreator = data -> {
            String code = data.get(0);
            String name = data.get(1);
            String country = data.get(2);
            String type = data.get(3);
            double latitude = Double.parseDouble(data.get(4));
            double longitude = Double.parseDouble(data.get(5));
            return new Station(code, name, country, type, latitude, longitude);
        };

        LambdaReader<Station> stationReader = null;
        try {
            stationReader = new LambdaReader<>("./resources/stations.csv", ",", stationCreator, true);
            stations = stationReader.readObjects();
        } finally {
            if (stationReader != null) {
                stationReader.close();
            }
        }

    }


    /**
     * Loads track data from a CSV file using a LambdaReader.
     * Each entry in the file is created with use of the Creator interface.
     *
     * @throws IOException If an error occurs during file reading.
     */
    public void loadTracks() throws IOException {
        Creator<Track, MyArrayList<String>> trackCreator = data -> {
            String from = data.get(0);
            String to = data.get(1);
            int costUnit = Integer.parseInt(data.get(2));
            double distance = Double.parseDouble(data.get(3));
            return new Track(from, to, costUnit, distance);
        };
        LambdaReader<Track> trackReader = null;
        try {
            trackReader = new LambdaReader<>("./resources/tracks.csv", ",", trackCreator, true);
            tracks = trackReader.readObjects();
        } finally {
            if (trackReader != null) {
                trackReader.close();
            }
        }

    }

    /**
     * Finds a station by its code in the station map.
     * @param code The code of the station to find.
     * @return The Station object if found, or null if not found.
     */

    public Station findStationByCode(String code) {
        try {
            return stationMap.get(code.trim());
        } catch (KeyNotFoundException e) {
            System.out.println("Station with code " + code + " not found.");
            return null;
        }
    }

    /**
     * Retrieves a list of stations that match the specified name, including prefix matches.
     * @param name The station name or part of it to search for.
     * @return A list of stations matching the name or prefix, or an empty list if none found.
     */

    public MyArrayList<Station> getStationsByName(String name) {
        MyArrayList<Station> matchingStations = new MyArrayList<>();


        if (stationsTree.contains(name)) {
            matchingStations.addLast(stationsTree.get(name));
        } else {

            MyArrayList<Station> prefixMatches = stationsTree.getByPrefix(name.toLowerCase());
            for (int i = 0; i < prefixMatches.size(); i++) {
                matchingStations.addLast(prefixMatches.get(i));
            }
        }

        if (matchingStations.isEmpty()) {
            System.out.println("Station with name " + capitalize(name) + " not found.");
        }

        return matchingStations;

    }


    /**
     * Retrieves a list of stations by their type, sorted alphabetically by name using the quickSort.
     * @param type The type of station.
     * @return A list of stations matching the type, sorted by name.
     */

    public MyArrayList<Station> getStationsByType(String type) {
        MyArrayList<Station> filteredStations = new MyArrayList<>();

        for (Station station : stations) {
            if (station.getType().equalsIgnoreCase(type)) {
                filteredStations.addLast(station);
            }
        }

        filteredStations.quickSort(Comparator.comparing(Station::getName));
        return filteredStations;
    }

    /**
     * Retrieves the shortest path between two stations using their names, using Dijkstra's algorithm.
     * @param startStation The name of the starting station.
     * @param endStation The name of the destination station.
     * @return A list of directed edges representing the shortest path, or null if no path exists.
     */

    public MyArrayList<SaxGraph.DirectedEdge<String>> getTheShortestPathBetweenTwoStations(String startStation, String endStation) {
        MyArrayList<SaxGraph.DirectedEdge<String>> path;

        String startCode = null;
        String endCode = null;

        try {
            startCode = stationNameToCodeMap.get(startStation.toLowerCase());
        } catch (KeyNotFoundException e) {
            System.out.println("Station with name " + startStation + " not found.");
        }

        try {
            endCode = stationNameToCodeMap.get(endStation.toLowerCase());
        } catch (KeyNotFoundException e) {
            System.out.println("Station with name " + endStation + " not found.");
        }


        if (startCode == null || endCode == null) {
            System.out.println("Cannot proceed with pathfinding as one or both stations were not found.");
            return null;
        }


        SaxGraph<String> dijkstraGraph = railNetworkGraph.shortestPathsDijkstra(startCode);
        try {
            path = (MyArrayList<SaxGraph.DirectedEdge<String>>) railNetworkGraph.backTrackDijkstra(dijkstraGraph, startCode, endCode);
        } catch (IllegalStateException e) {
            System.out.println("No path found between " + startStation + " and " + endStation + ".");
            return null;
        }


        return path;
    }

    /**
     * Helper method to capitalize the first letter of each word in a station name.
     * @param name The station name to capitalize.
     * @return The capitalized station name.
     */

    public String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }

        String[] words = name.split(" ");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return capitalized.toString().trim();
    }


    /**
     * Filters and retrieves only the stations located in the Netherlands from the stations' list.
     * @return A MyArraylist of Netherlands stations.
     */

    private MyArrayList<Station> filterOnlyNetherlandsStations() {
        MyArrayList<Station> filteredNLStations = new MyArrayList<>();
        for (Station station : stations) {
            if (station.getCountry().equalsIgnoreCase("NL")) {
                filteredNLStations.addLast(station);
            }
        }
        return filteredNLStations;
    }

    /**
     * Helper method which checks if a track is bidirectional by verifying the existence of its reverse.
     * @param track The track to check.
     * @return True if the track is bidirectional, false otherwise.
     */

    private boolean isBidirectionalTrack(Track track) {
        for (Track reverseTrack : tracks) {
            if (reverseTrack.getFrom().equals(track.getTo()) &&
                    reverseTrack.getTo().equals(track.getFrom()) &&
                    reverseTrack.getDistanceInKilometers() == track.getDistanceInKilometers()) {
                return true;
            }
        }
        return false;
    }




}
