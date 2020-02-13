package main.java.geocoder;

public class RequestParser {

    private String jsonString;

    RequestParser(String json) {
        this.jsonString = json;
    }

    public String getRawJSON() {
        return jsonString;
    }

    // TODO: Add parsing functionality for the data we want

}
