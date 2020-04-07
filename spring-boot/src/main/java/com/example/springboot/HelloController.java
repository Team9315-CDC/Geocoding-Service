package com.example.springboot;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

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

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.core.io.FileSystemResource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController {

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}


	@GetMapping("/test/temp.csv")
	 void downloadCsv(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; file=temp.csv");
	}

	@PostMapping("/test")
	File test(@RequestParam("file") MultipartFile file) throws Exception{
		File newFile = new File("./temp.csv");
		// validate file
        if (file.isEmpty()) {
            System.out.println("Please select a CSV file to upload.");
        } else {
			try {
				System.out.println(file.getContentType());
				String benchmark = "Public_AR_Current";
				String batchURL = "https://geocoding.geo.census.gov/geocoder/locations/addressbatch";
				// TODO: stream csv file to BatchCurl.java for batch processing
//				Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				CloseableHttpClient httpClient = HttpClients.createDefault();
				try {

					HttpPost request = new HttpPost(batchURL);
					MultipartEntityBuilder builder2 = MultipartEntityBuilder.create();
					builder2.addTextBody("benchmark", benchmark, ContentType.TEXT_PLAIN);
					File f = convert(file);
					FileInputStream is = new FileInputStream(f);
					// AnalyzeInputStream(is);

					builder2.addBinaryBody("addressFile", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM,
							f.getName());
					HttpEntity multipart = builder2.build();
					request.setEntity(multipart);
					CloseableHttpResponse response = httpClient.execute(request);
					System.out.println("Executing batch request ... ");
					try {
						HttpEntity responseEntity = response.getEntity();
						System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
						if (responseEntity != null) {
//							String strResponse = EntityUtils.toString(responseEntity);
//							System.out.println(strResponse);

							byte[] responseBody = EntityUtils.toString(responseEntity).getBytes();

							try (FileOutputStream fop = new FileOutputStream(newFile)) {
								if (!newFile.exists()) {
									newFile.createNewFile();
								}
								System.out.println("Initializing write.....");
								fop.write(responseBody);
								fop.flush();
								fop.close();
								System.out.println("Finished writing CSV to file " + newFile.getPath());
//								return "{ \"result\": \"file of type " + file.getContentType() + " received and geocoded\"}";
								ResponseEntity.ok()
										.contentType(MediaType.parseMediaType("text/csv"))
										.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; file=\"" + newFile.getName() + "\"")
										.body(new FileSystemResource(newFile));
								return newFile;
							} catch (IOException e) {
								e.printStackTrace();
//								System.out.println("exception occured: " + e.toString());

							}
						}
					} finally {
						response.close();
					}
				} finally {
					httpClient.close();
				}
			} catch (Exception e) {
				System.out.println("exception occured: " + e.toString());
			}
		}
		return newFile;

	}

}
