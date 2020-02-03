package main.java.geocoder;

import java.lang.StringBuilder;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class GeocodeRequest {

    private static final String baseUrl = "https://geocoding.geo.census.gov/geocoder/locations/";
    private static final String parameters = "&benchmark=Public_AR_Current&format=json";
    private StringBuilder urlBuilder = new StringBuilder(baseUrl);
    private Address address;
    private String response;

    public GeocodeRequest(Address address, SearchType st) {
        this.address = address;
        buildURL(st);
    }

    public GeocodeRequest(Address address) {
        this.address = address;
        if (this.address.isOneLine()) {
            buildURL(SearchType.ONE_LINE_ADDRESS);
        } else {
            buildURL(SearchType.FULL_ADDRESS);
        }
    }

    public void buildURL(SearchType st) {
        switch (st) {
            case ONE_LINE_ADDRESS:
                urlBuilder.append("onelineaddress?address=");
                break;
            case FULL_ADDRESS:
                urlBuilder.append("address?");
                //TODO: add the other fields for a more complete address
                break;
        }
        urlBuilder.append(address.getAddress());
        urlBuilder.append(parameters);

        System.out.println("URL: " + urlBuilder.toString());
    }

    public boolean submitRequest() throws Exception {
        // TODO: Add error handling

        URL request = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection)request.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            System.out.println("HTTP Code: " + responseCode);
            return false;
        }
        Scanner sc = new Scanner(request.openStream());
        while (sc.hasNext()) {
            response += sc.nextLine();
        }
        sc.close();
        return true;
    }

    public void printResponse() {
        System.out.println(response);
    }

    public String getResponse() {
        return response;
    }

    public enum SearchType {
        ONE_LINE_ADDRESS,
        FULL_ADDRESS
    }
}
