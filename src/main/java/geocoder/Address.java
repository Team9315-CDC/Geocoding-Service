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

    public Address(String street, String city, String state, String zip) {
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
}