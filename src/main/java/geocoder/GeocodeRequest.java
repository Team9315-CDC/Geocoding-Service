package main.java.geocoder;

import java.lang.StringBuilder;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class GeocodeRequest {

    private static final String baseUrl = "https://geocoding.geo.census.gov/geocoder/locations/";
    // TODO: Figure out what benchmark is best to use; perhaps make a parameter for the class
    private static final String parameters = "&benchmark=9&format=json";
    private StringBuilder urlBuilder = new StringBuilder(baseUrl);
    private boolean builtAddress;
    private Address address;
    private String response = "";

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
        if (!builtAddress) {
            switch (st) {
                case ONE_LINE_ADDRESS:
                    urlBuilder.append("onelineaddress?address=");
                    urlBuilder.append(address.getAddress());
                    break;
                case FULL_ADDRESS:
                    urlBuilder.append("address?street=");
                    urlBuilder.append(address.getStreet());
                    if (address.getCity() != null) urlBuilder.append("&city=").append(address.getCity());
                    if (address.getState() != null) urlBuilder.append("&state=").append(address.getState());
                    if (address.getZip() != null) urlBuilder.append("&zip=").append(address.getZip());
                    break;
            }
            urlBuilder.append(parameters);
            System.out.println("URL: " + urlBuilder.toString());
        }
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
        response = sc.nextLine();
        sc.close();
        return true;
    }

    public void printResponse() {
        System.out.println("JSON Response received: " + response);
    }

    public String getResponse() {
        return response;
    }

    public enum SearchType {
        ONE_LINE_ADDRESS,
        FULL_ADDRESS
    }
}
