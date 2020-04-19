package main.java.geocoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SQLQuery {

    private Connection serverConn;

    public SQLQuery() {
    }

    public void serverConnect() throws SQLException {
        Scanner usrIn = new Scanner(System.in);
        // TODO: FIX THIS
        System.out.println("Server:");
        String connectionURL = "jdbc:sqlserver://localhost;"
                + "database="  + ";";
        System.out.println("Username:");
        String usrName = usrIn.nextLine();
        connectionURL = connectionURL + "user=" + usrName + ";";
        System.out.println("Password:");
        String pass = usrIn.nextLine();
        connectionURL = connectionURL + "password=" + pass + ";"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";
        serverConn = DriverManager.getConnection(connectionURL);
        usrIn.close();
    }

    public void autoConnect(String serverName, String databaseName, String username, String password) throws SQLException {
        String connectionUrl =
                "jdbc:sqlserver://" + serverName + ";"
                        + "database=" + databaseName + ";"
                        + "user=" + username + ";"
                        + "password=" + password + ";"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
        serverConn = DriverManager.getConnection(connectionUrl);
    }

    public int startNewBatchQuery() throws SQLException, IOException {
        if (!serverConn.isValid(30)) {
            throw new ConnectionFailureException("Lost connection to the SQL server!");
        }
        Statement statement = serverConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String query = "SELECT postal_locator_uid, street_addr1, city_desc_txt, state_cd, zip_cd, geocode_match_ind\n" +
                "FROM Postal_locator\n" +
                "WHERE (street_addr1 IS NOT NULL AND NOT street_addr1 = '')\n" +
                    "AND (NOT (geocode_match_ind='S' OR geocode_match_ind='M' OR geocode_match_ind='F') " +
                        "OR geocode_match_ind is NULL);" +
                "ORDER BY postal_locator_uid\n" +
                "OFFSET 0 ROWS\n" +
                "FETCH FIRST 10000 ROWS ONLY;";
        ResultSet queryResults = statement.executeQuery(query);
        System.out.println("Query sucessful, generating CSV...\n");
        Map<Integer, Integer> idIndexes = generateTempIDs(queryResults);
        File csvFile = generateCSV(queryResults, idIndexes);
        csvFile.deleteOnExit();
        System.out.println("CSV generated. Submitting geocoding request...");
        for (Map.Entry<Integer, Integer> pair : idIndexes.entrySet()) {
            System.out.println(pair.getKey() + "-->" + pair.getValue());
        }
        queryResults.last();
        int numOfResults = queryResults.getRow();
        queryResults.close();
        return numOfResults;
    }

    private Map<Integer, Integer> generateTempIDs(ResultSet results) throws SQLException {
        Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
        int index = 0;
        results.beforeFirst();
        while (results.next()) {
            ids.put(results.getInt("postal_locator_uid"), index++);
        }
        return ids;
    }

    private File generateCSV(ResultSet results, Map<Integer, Integer> idPairings) throws IOException, SQLException {
        File csv = File.createTempFile("geocoding_",".csv");
        csv.deleteOnExit();
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csv));
        results.beforeFirst();
        while (results.next()) {
            StringBuilder line = new StringBuilder(idPairings.get(results.getInt("postal_locator_uid")) + ",");
            line.append(results.getString("street_addr1")).append(",");
            String city, state, zip;
            if ((city = results.getString("city_desc_txt")) != null) {
                line.append(city);
            }
            line.append(",");
            if ((state = results.getString("state_cd")) != null) {
                line.append(state);
            }
            line.append(",");
            if ((zip = results.getString("zip_cd")) != null) {
                line.append(zip);
            }
            csvWriter.write(line.toString());
            csvWriter.newLine();
        }
        csvWriter.close();
        return csv;
    }

}

class ConnectionFailureException extends SQLException {
    public ConnectionFailureException(String errorMsg) {
        super(errorMsg);
    }
}
