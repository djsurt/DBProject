package gui.relations;

public class Location {

    private String country;
    private String city;
    private String state;
    private int locationID;

    public Location(String country, String city, String state, int locationID) {
        this.country = country;
        this.city = city;
        this.state = state;
        this.locationID = locationID;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getLocationID() {
        return locationID;
    }
}
