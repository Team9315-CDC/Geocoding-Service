package main.java.geocoder;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SQLQuery {

    private Connection serverConn;

    public SQLQuery() {
    }

    /**
     * Connects to an SQL database using user supplied credentials
     * @throws SQLException
     */
    public void serverConnect() throws SQLException {
        Scanner usrIn = new Scanner(System.in);
        // TODO: FIX THIS
        System.out.println("Server:");
        String connectionURL = "jdbc:sqlserver://" + usrIn.nextLine() + ";";
        System.out.println("\nDatabase:");
        connectionURL += "database=" + usrIn.nextLine() + ";";
        System.out.println("\nUsername:");
        connectionURL += "user=" + usrIn.nextLine() + ";";
        System.out.println("\nPassword:");
        connectionURL += "password=" + usrIn.nextLine() + ";"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";
        serverConn = DriverManager.getConnection(connectionURL);
        usrIn.close();
    }

    /**
     * Connects to an SQL database without prompting the user for credentials
     * Must be recompiled to work
     * @throws SQLException
     */
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

    /**
     * Start a new batch query of the Postal_locator table and store results in the Geocoding_Results table
     * @return the number of results read from the Postal_locator table
     * @throws Exception
     */
    public int startNewBatchQuery() throws Exception {
        if (!serverConn.isValid(30)) {
            throw new ConnectionFailureException("Lost connection to the SQL server!");
        }
        Statement statement = serverConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String query = "SELECT postal_locator_uid, street_addr1, city_desc_txt, state_cd, zip_cd, geocode_match_ind\n" +
                "FROM Postal_locator\n" +
                "WHERE (street_addr1 IS NOT NULL AND NOT street_addr1 = '')\n" +
                    "AND (NOT (geocode_match_ind='S' OR geocode_match_ind='N' OR geocode_match_ind='F') " +
                        "OR geocode_match_ind is NULL)\n" +
                "ORDER BY postal_locator_uid\n" +
                "OFFSET 0 ROWS\n" +
                "FETCH FIRST 10000 ROWS ONLY;";
        ResultSet queryResults = statement.executeQuery(query);
        System.out.println("Query sucessful, generating CSV...");

        Map<Long, Integer> idIndexes = generateTempIDs(queryResults);
        File csvFile = generateCSV(queryResults, idIndexes);
        System.out.println("CSV generated. Submitting geocoding request...");

        int numOfResults = 0;
        BatchCurl batch = new BatchCurl("");
        if (batch.batchRequest(csvFile)) {
            System.out.println("Successfully geocoded addresses. Saving results...");
            File geocodeResults = batch.getBatchResults();
            geocodeResults.deleteOnExit();
            Map<Integer, String[]> csvExpanded = parseCSV(geocodeResults);
            queryResults.beforeFirst();
            int count = 0;
            PreparedStatement geoResultTable = serverConn.prepareStatement(
                    "INSERT INTO Geocoding_Result(geocoding_result_uid, postal_locator_uid, add_time," +
                            "last_chg_time, result_type, num_matches," +
                            "street_addr1, city, state, zip_cd, true_longitude, true_latitude)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            while (queryResults.next()) {
                long postId = queryResults.getLong("postal_locator_uid");
                String[] dataSet = csvExpanded.get(idIndexes.get(postId));
                if (dataSet[5].equals("No_Match")) {
                    queryResults.updateString("geocode_match_ind", "F");
                    queryResults.updateRow();
                } else if (dataSet[5].equals("Match")) {
                    String status;
                    if (dataSet[6].equals("Exact")) {
                        status = "S";
                    } else {
                        status = "N";
                    }
                    queryResults.updateString("geocode_match_ind", status);
                    queryResults.updateRow();
                    updateGeocodedResult(geoResultTable, postId, dataSet, status);
                    count++;
                }
                if (count % 100 == 0) {
                    count = 0;
                    geoResultTable.executeBatch();
                }
            }
            geoResultTable.executeBatch();
            System.out.println("Successfully stored " + count + " results.");
            geoResultTable.close();
            queryResults.last();
            numOfResults = queryResults.getRow();
        }
        queryResults.close();
        statement.close();
        return numOfResults;
    }

    /*
        For security, the IDs used in the database are replaced with a different number
        before sending it to the US Census API
     */
    private Map<Long, Integer> generateTempIDs(ResultSet results) throws SQLException {
        Map<Long, Integer> ids = new HashMap<Long, Integer>();
        int index = 1;
        results.beforeFirst();
        while (results.next()) {
            ids.put(results.getLong("postal_locator_uid"), index++);
        }
        return ids;
    }

    /*
        This CSV is the one to be sent to the geocoder API to be geocoded
     */
    private File generateCSV(ResultSet results, Map<Long, Integer> idPairings) throws IOException, SQLException {
        File csv = File.createTempFile("geocoding_",".csv");
        csv.deleteOnExit();
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csv));
        results.beforeFirst();
        while (results.next()) {
            StringBuilder line = new StringBuilder(idPairings.get(results.getLong("postal_locator_uid")) + ",");
            line.append(results.getString("street_addr1")).append(",");
            String city, zip;
            int state;
            if ((city = results.getString("city_desc_txt")) != null) {
                line.append(city);
            }
            line.append(",");
            if ((state = results.getInt("state_cd")) != 0) {
                line.append(State.valueOfCode(state));
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

    /*
        This parses the CSV received from the geocoder API. The resulting String array has the following entires:
        temp_id, input_street_addr, input_city, input_state, input_zip_code, match, exact_match, output_street_addr,
        output_city, output_state, output_zip, longitude, latitude, tigerLineID, tigerLineSide
     */
    private Map<Integer, String[]> parseCSV(File geoResults) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(geoResults));
        Map<Integer, String[]> csvEntries = new HashMap<>();
        String line;
        while ((line = csvReader.readLine()) != null) {
            String[] contents = line.replace("\"","").split(",");
            if (contents.length > 6) {
                contents[8] = contents[8].replace(" ", "");
                contents[9] = contents[9].replace(" ", "");
                contents[10] = contents[10].replace(" ", "");
            }
            csvEntries.put(Integer.parseInt(contents[0]), contents);
        }
        csvReader.close();
        return csvEntries;
    }

    /*
        Updates the Geocoding_Results table with successful matches. Below is the list of fields set and the parameter order
        geocoding_result_uid ==> 1
        postal_locator_uid ==> 2
        add_time ==> 3
        last_chg_time ==> 4
        result_type ==> 5
        num_matches ==> 6
        street_addr1 ==> 7
        city ==> 8
        state ==> 9
        zip_cd ==> 10
        longitude ==> 11
        latitude ==> 12
     */
    private void updateGeocodedResult(PreparedStatement sqlStatement, long id, String[] content, String resultType) throws SQLException {
        sqlStatement.setLong(1, id);
        sqlStatement.setLong(2, id);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        sqlStatement.setTimestamp(3, now);
        sqlStatement.setTimestamp(4, now);
        sqlStatement.setString(5, resultType);
        sqlStatement.setShort(6, (short) 1);
        sqlStatement.setString(7, content[7]);
        sqlStatement.setString(8, content[8]);
        sqlStatement.setString(9, content[9]);
        sqlStatement.setString(10, content[10]);
        sqlStatement.setDouble(11, Double.parseDouble(content[11]));
        sqlStatement.setDouble(12, Double.parseDouble(content[12]));
        sqlStatement.addBatch();
    }

}

class ConnectionFailureException extends SQLException {
    public ConnectionFailureException(String errorMsg) {
        super(errorMsg);
    }
}
