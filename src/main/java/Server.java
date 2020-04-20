package main.java;

import main.java.geocoder.*;
import java.util.Arrays;
import java.io.IOException;
import main.java.geocoder.SQLQuery;
import java.sql.SQLException;

public class Server {


    public static void main(String[] args) {

        // Single address geocoding script
        Address sample = new Address("4600+Silver+Hill+Rd", "Suitland", "MD", "20746");
        // Address sample = new Address("4600+Silver+Hill+Rd%2C+Suitland%2C+MD+20746");
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

        // Batch geocoding script with local CSV
        BatchCurl test = new BatchCurl("LocalTestInstance_v3.csv");
        boolean batchStatus = false;
        try {
            batchStatus = test.batchRequest();
        } catch (Exception e) {
            System.out.println("Batch Import Error");
            System.out.println(e.toString());
        }

        SQLQuery batchQuery = new SQLQuery();
        try {
            // Update server credentials if you want to use autoConnect
            batchQuery.autoConnect("localhost", "NBS_ODSE", "SA", "Admin1234!?");
            //batchQuery.serverConnect("NBS_ODSE");
            int currCount, totalCount = 0;
            do {
                // Max size of a batch query is 10000
                // Attempts to pull max size every time
                // Loops until it can't find any more results (no longer the max results)
                currCount = batchQuery.startNewBatchQuery();
                totalCount += currCount;
            } while (currCount == 10000);
            System.out.println("Total Addresses Checked: " + totalCount);
        } catch (SQLException | IOException e) {
            System.out.println("SQL/Temp file error occurred");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("General error occurred");
            e.printStackTrace();
        }

    }

}