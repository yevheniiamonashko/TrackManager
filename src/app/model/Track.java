package app.model;

public class Track {

    private final String from;
    private final String to;
    private final int costUnit;
    private final double distanceInKilometers;

    public Track(String from, String to, int costUnit, double distanceInKilometers) {
        this.from = from;
        this.to = to;
        this.costUnit = costUnit;
        this.distanceInKilometers = distanceInKilometers;

    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getCostUnit() {
        return costUnit;
    }

    public double getDistanceInKilometers() {
        return distanceInKilometers;
    }


}


