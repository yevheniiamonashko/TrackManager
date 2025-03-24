package nl.saxion.cds.solution.model;

public class Station {

    private final String code;
    private final String name;
    private final String country;
    private final String type;
    private final double latitude;
    private final double longitude;

    public Station(String code, String name, String country, String type, double latitude, double longitude) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Retrieves the full name of the country based on the provided country code in CSV file.
     * This method uses a switch statement to map specific country codes to their respective full names for readability.
     *
     * @param country The abbreviated country code.
     * @return The full name of the country corresponding to the provided code.
     *         If the code is not recognized, returns "Unknown Country".
     */

    private String getCountryFullName(String country) {
        return switch (country) {
            case "NL" -> "Netherlands";
            case "D" -> "Germany";
            case "B" -> "Belgium";
            case "F" -> "France";
            case "A" -> "Austria";
            case "CH" -> "Switzerland";
            case "GB" -> "United Kingdom";
            default -> "Unknown Country";
        };
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Station Information:").append("\n");
        ;
        sb.append("--------------------").append("\n");
        ;
        sb.append("Station code: ").append(code).append("\n");
        sb.append("Station name: ").append(name).append("\n");
        sb.append("Station country: ").append(getCountryFullName(country)).append("\n");
        sb.append("Station type: ").append(type).append("\n");
        return sb.toString();
    }
}
