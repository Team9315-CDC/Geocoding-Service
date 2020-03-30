package main.java;

import main.java.geocoder.*;
import java.util.Arrays;

public class Server {


    public static void main(String[] args) {

//        Address sample = new Address("4600+Silver+Hill+Rd%2C+Suitland%2C+MD+20746");
        Address sample = new Address("4600+Silver+Hill+Rd", "Suitland", "MD", "20746");
        GeocodeRequest gr = new GeocodeRequest(sample);
        boolean status = false;
        try {
            status = gr.submitRequest();
        } catch (Exception e) {
            System.out.println("Unexpected exception!");
        }
        if (status) {
            gr.printResponse();
            RequestParser rp = new RequestParser(gr.getResponse());
            System.out.println("Address latitude and longitude: " + Arrays.toString(rp.getCoordinates()));

        } else {
            System.out.println("Something went wrong?");
        }
    }

}