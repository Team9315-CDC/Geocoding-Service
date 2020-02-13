package main.java.geocoder;

public class Address {

    private String oneLine;
    private String street;
    private String city;
    private String state;
    private String zip;
    private boolean isOneLine = true;

    public Address(String address) {
        this.oneLine = address;
    }

    /**
     * Constructor for creating more detailed addresses
     * @param street    street must be initialized to a valid string for a search to be performed
     */
    public Address(String street, String city, String state, String zip) {
        if (street == null) {
            throw new IllegalArgumentException("Street must be a valid string for verification!");
        }
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.isOneLine = false;
    }

    public String getAddress() {
        return oneLine;
    }

    public boolean isOneLine() {
        return isOneLine;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }
}