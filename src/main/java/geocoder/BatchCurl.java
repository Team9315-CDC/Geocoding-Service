// curl command example
// curl --form addressFile=@LocalTestInstance_v3.csv --form benchmark=Public_AR_Current https://geocoding.geo.census.gov/geocoder/locations/addressbatch --output testCurl_ARCURRENT.csv

package main.java.geocoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BatchCurl {

    public String path;
    public String benchmark = "Public_AR_Current";
    public String batchURL = "https://geocoding.geo.census.gov/geocoder/locations/addressbatch";

    public BatchCurl(String path) {
        this.path = path;
    }
    public boolean batchRequest(File importFile) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            HttpPost request = new HttpPost(batchURL);
            MultipartEntityBuilder builder2 = MultipartEntityBuilder.create();
            builder2.addTextBody("benchmark", benchmark, ContentType.TEXT_PLAIN);
            File f = importFile;
            FileInputStream is = new FileInputStream(f);

            builder2.addBinaryBody("addressFile", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM,
                    f.getName());
            HttpEntity multipart = builder2.build();
            request.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(request);

            try {
                HttpEntity responseEntity = response.getEntity();
                System.out.println(response.getStatusLine().getStatusCode());
                if (responseEntity != null) {
                    byte[] responseBody = EntityUtils.toString(responseEntity).getBytes();

                    File file = new File("testing data/BatchImportResults.csv");

                    try (FileOutputStream fop = new FileOutputStream(file)) {

                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        System.out.println("Initializing write.....");

                        fop.write(responseBody);
                        fop.flush();
                        fop.close();

                        System.out.println("Finished writing CSV to file " + file.getPath());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
        return true;
    }
    public boolean batchRequest() throws Exception {
        System.out.println("Starting the CURL" + " for " + path);
        System.out.println(new File(".").exists());
        String fullpath = new File("testing data/" + path).getAbsolutePath();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            HttpPost request = new HttpPost(batchURL);
            MultipartEntityBuilder builder2 = MultipartEntityBuilder.create();
            builder2.addTextBody("benchmark", benchmark, ContentType.TEXT_PLAIN);
            File f = new File(fullpath);
            FileInputStream is = new FileInputStream(f);

            builder2.addBinaryBody("addressFile", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM,
                    f.getName());
            HttpEntity multipart = builder2.build();
            request.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(request);

            try {
                HttpEntity responseEntity = response.getEntity();
                System.out.println(response.getStatusLine().getStatusCode());
                if (responseEntity != null) {
                    byte[] responseBody = EntityUtils.toString(responseEntity).getBytes();
                    // System.out.println(responseBody);

                    File file = new File("testing data/BatchImportResults.csv");

                    try (FileOutputStream fop = new FileOutputStream(file)) {

                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        System.out.println("Initializing write.....");

                        fop.write(responseBody);
                        fop.flush();
                        fop.close();

                        System.out.println("Finished writing CSV to file " + file.getPath());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
        return true;
    }
}