package app;

import collection.SaxGraph;
import custom_data_structures.MyArrayList;
import app.model.Station;

import java.util.Scanner;

public class Application {

    Scanner scanner = new Scanner(System.in);
    TrackManager manager = new TrackManager();

    public static void main(String[] args) {

        Application app = new Application();

        app.handleMenu();

    }


    /**
     * Prints the main menu for the Rail Network Manager application.
     * This menu lists the available options that the user can select.
     */


    public void printMenu() {
        System.out.println("Welcome to the Rail Network Manager!");
        System.out.println("Menu:");
        System.out.println("1. Get information about station based on station code");
        System.out.println("2. Get information about station based on name");
        System.out.println("3. Get all stations based on station type");
        System.out.println("4. Get the shortest path between two stations");
        System.out.println("5. Get the minimum cost spanning tree for Netherlands stations only");
        System.out.println("6. Get the minimum cost spanning tree for all stations");
        System.out.println("0. Exit");
        System.out.print("Please enter your choice: ");

    }

    /**
     * Handles the menu interaction.
     * Continuously prompts the user to select an option until they choose to exit.
     * Validates the user's input and calls the corresponding method based on the choice.
     */

    public void handleMenu() {
        int choice = -1;
        while (choice != 0) {
            printMenu();
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 0 && choice <= 6) {
                    switch (choice) {
                        case 1:
                            retrieveStationByCode();
                            break;
                        case 2:
                            retrieveStationByName();
                            break;
                        case 3:
                            retrieveStationByType();
                            break;
                        case 4:
                            retrieveTheShortestPathBetweenTwoStations();
                            break;

                        case 5:
                            showMCSTForNetherlands();
                            break;

                        case 6:
                            showMCSTForAllStations();
                        case 0:
                            System.out.println("Goodbye!");
                            break;
                    }
                } else {
                    System.out.println("Invalid choice. Please select option from the menu(0-6):");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number(0-6):");
                scanner.next();
            }
        }
        scanner.close();

    }


    /**
     * Prompts the user to enter a station code and calls TrackManager method `findStationByCode`
     * to retrieve information about the station.
     * Validates the station code and displays the station details if found.
     */

    public void retrieveStationByCode() {
        String code = "";
        while (code.isEmpty()) {
            System.out.print("Please enter the station code: ");
            code = scanner.nextLine().trim();

            if (code.isEmpty()) {
                System.out.println("Station code can not be empty. Please enter a station code");
            } else if (!validateStringInput(code)) {
                System.out.println("Invalid station code. Station code should contain only letters. Please try again.");
                code = "";
            }

        }


        Station station = manager.findStationByCode(code.toUpperCase());
        if (station != null) {
            System.out.println(station);
        }


    }


    /**
     * Prompts the user to enter a station name
     * and calls the TrackManager method `getStationsByName`
     * to retrieve information about stations that match the name or part of it.
     * Validates the station name and displays the details of matching stations.
     */

    public void retrieveStationByName() {
        String name = "";
        while (name.isEmpty()) {
            System.out.print("Please enter the station name: ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Station name can not be empty. Please enter a station name");
            } else if (!validateStringInput(name)) {
                System.out.println("Invalid station name. Station type should contain only letters. Please try again.");
                name = "";
            }


        }


        MyArrayList<Station> stationsByName = manager.getStationsByName(name.toLowerCase());
        if (!stationsByName.isEmpty()) {
            System.out.println("Stations found based on your request:");
            for (Station station : stationsByName) {
                System.out.println(station);
            }
        }

    }

    /**
     * Prompts the user to enter a station type
     * and calls TrackManager method `getStationsByType` to retrieve all stations with the specified type.
     * Validates the station type and displays the names and codes of matching stations.
     */

    public void retrieveStationByType() {
        String type = "";
        while (type.isEmpty()) {
            System.out.print("Please enter the station type: ");
            type = scanner.nextLine().trim();

            if (type.isEmpty()) {
                System.out.println("Station type cannot be empty. Please enter a station type");
            } else if (!validateStringInput(type)) {
                System.out.println("Invalid station type. Station type should contain only letters. Please try again.");
                type = "";
            }

        }


        MyArrayList<Station> stationsByType = manager.getStationsByType(type);
        if (!stationsByType.isEmpty()) {
            System.out.println("Stations with type " + type + ":");
            for (Station station : stationsByType) {
                System.out.println("Station name: " + station.getName() + "  Station code:" + station.getCode());
            }
        } else {
            System.out.println("Stations with type " + type + " was not found");
        }


    }

    /**
     * Prompts the user to enter the names of two stations
     * and calls the TrackManager method `getTheShortestPathBetweenTwoStations`
     * to calculate the shortest path between them.
     * Validates both station names and displays the route and total distance if a path is found.
     */

    public void retrieveTheShortestPathBetweenTwoStations() {
        String startStation = "";
        String endStation = "";


        while (startStation.isEmpty() || !validateStringInput(startStation)) {
            System.out.print("Please enter the starting station name: ");
            startStation = scanner.nextLine().trim();

            if (startStation.isEmpty()) {
                System.out.println("Starting station name cannot be empty. Please try again.");
            } else if (!validateStringInput(startStation)) {
                System.out.println("Invalid input. Station name should contain only letters. Please try again.");
                startStation = "";
            }
        }

        while (endStation.isEmpty() || !validateStringInput(endStation)) {
            System.out.print("Please enter the ending station name: ");
            endStation = scanner.nextLine().trim();

            if (endStation.isEmpty()) {
                System.out.println("Ending station name cannot be empty. Please try again.");
            } else if (!validateStringInput(endStation)) {
                System.out.println("Invalid input. Station name should contain only letters. Please try again.");
                endStation = "";
            }
        }
        MyArrayList<SaxGraph.DirectedEdge<String>> path = manager.getTheShortestPathBetweenTwoStations(startStation, endStation);

        if (path != null) {
            StringBuilder route = new StringBuilder();


            for (int i = 0; i < path.size(); i++) {
                SaxGraph.DirectedEdge<String> edge = path.get(i);
                String fromStationName = manager.findStationByCode(edge.from()).getName();
                String toStationName = manager.findStationByCode(edge.to()).getName();


                if (i == 0) {
                    route.append(fromStationName);
                }

                route.append(" -> ").append(toStationName);
            }

            double totalDistance = path.get(path.size() - 1).weight();

            System.out.println("Shortest path from " + manager.capitalize(startStation) + " to " + manager.capitalize(endStation) + ":");
            System.out.println("Route: " + route);
            System.out.println("Total distance: " + totalDistance + " km");
        } else {
            System.out.println("Please try again.");
        }

    }

    /**
     * Displays the Minimum Cost Spanning Tree (MCST) for stations located in the Netherlands.
     * Calls the TrackManager's display method `displayMCSTForNetherlandsStations` to show the MCST.
     */

    public void showMCSTForNetherlands() {
        System.out.println("Minimum cost spanning tree for stations in Netherlands");
        manager.displayMCSTForNetherlandsStations();
    }

    /**
     * Displays the Minimum Cost Spanning Tree (MCST) for all stations in the rail network.
     * Calls the TrackManager's display method `displayMCSTForAllStations` to show the MCST.
     */

    private void showMCSTForAllStations() {
        System.out.println("Minimum cost spanning tree for all stations");
        manager.displayMCSTForAllStations();
    }

    /**
     * Helper method to validate if the input string contains only letters, spaces, hyphens, or parentheses.
     * This method is used to ensure valid user input for station codes, names, and types.
     *
     * @param input The string to validate.
     * @return true if the input is valid, false otherwise.
     */

    private boolean validateStringInput(String input) {
        return input.matches("[\\p{L}\\s\\-\\(\\)]+");
    }


}
