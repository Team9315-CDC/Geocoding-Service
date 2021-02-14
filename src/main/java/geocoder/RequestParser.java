package main.java.geocoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RequestParser {

    private String jsonString;
    private JsonObject jsonObject;

    public RequestParser(String jsonString) {

        this.jsonString = jsonString;
        jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
    }

    public String getRawJSON() {
        return jsonString;
    }
    
    /*
    addressMatchesArray is the array of matches an address input can be associated to. 
    This can be a useful array to have if you want all the addresses that were matched with the input. 
    */
    public double getLat() {
        JsonArray addressMatchesArray = jsonObject.getAsJsonObject("result").getAsJsonArray("addressMatches");
        JsonObject firstMatchedAddress = addressMatchesArray.get(0).getAsJsonObject();
        JsonObject coordinates = firstMatchedAddress.getAsJsonObject("coordinates");
        double latitude = coordinates.get("x").getAsDouble();
        return latitude;
    }

    public double getLong() {
        JsonArray addressMatchesArray = jsonObject.getAsJsonObject("result").getAsJsonArray("addressMatches");
        JsonObject firstMatchedAddress = addressMatchesArray.get(0).getAsJsonObject();
        JsonObject coordinates = firstMatchedAddress.getAsJsonObject("coordinates");
        double longitude = coordinates.get("y").getAsDouble();
        return longitude;
    }

    public double[] getCoordinates() {
        return new double[] {getLat(), getLong()};
    }

}
