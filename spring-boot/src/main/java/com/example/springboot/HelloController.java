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
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

	@RequestMapping(value="/test")
	void test(@RequestParam("file") MultipartFile file, HttpServletResponse servletResponse) throws Exception{
		// validate file
		ResponseEntity<byte[]> filebyte;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		HttpHeaders httpHeaders = new HttpHeaders();
		filebyte = new ResponseEntity<byte[]>(bos.toByteArray(), httpHeaders, HttpStatus.CREATED);

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
							servletResponse.setContentType("application/octet-stream");
							servletResponse.setHeader("Content-Disposition", "attachment; file=temp.csv");
							File newFile = new File("./temp.csv");
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
//								ResponseEntity.ok()
//										.contentType(MediaType.parseMediaType("text/csv"))
//										.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; file=\"" + newFile.getName() + "\"")
//										.body(new FileSystemResource(newFile));
								System.out.println("Downloading.....");
//								InputStream istream = new FileInputStream(newFile);
//								System.out.println(istream.toString());
//								org.apache.commons.io.IOUtils.copy(istream, servletResponse.getOutputStream());
//								servletResponse.flushBuffer();

								System.out.println(newFile.getName());


								FileInputStream fileInputStream = new FileInputStream(newFile);
								org.apache.commons.io.IOUtils.copy(fileInputStream, servletResponse.getOutputStream());
								fileInputStream.close();
								servletResponse.flushBuffer();


							} catch (IOException e) {
								e.printStackTrace();

							}


//							File tempFile = File.createTempFile("tempGeocode", ".csv");
//							FileOutputStream fop = new FileOutputStream(tempFile);
//							fop.write(responseBody);
//							fop.flush();
//							fop.close();
//
//							FileInputStream fis = new FileInputStream(tempFile);
////							ByteArrayOutputStream bos = new ByteArrayOutputStream();
//							byte[] b = new byte[1024];
//							int n;
//							while ((n = fis.read(b)) != -1)
//							{
//								bos.write(b, 0, n);
//							}
//							fis.close();
//							bos.close();
////							HttpHeaders httpHeaders = new HttpHeaders();
//							String fileName = new String("Testing.csv".getBytes("UTF-8"), "iso-8859-1");
//							httpHeaders.setContentDispositionFormData("attachment", fileName);
//							httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//							filebyte = new ResponseEntity<byte[]>(bos.toByteArray(), httpHeaders, HttpStatus.CREATED);
//
//							System.out.println("FileByte Completed.");

//
//							System.out.println("Downloading File...");
//							org.apache.commons.io.IOUtils.copy(istream, servletResponse.getOutputStream());
//							servletResponse.flushBuffer();
//							servletResponse.setContentType("text/csv");
//							servletResponse.setHeader("Content-Disposition", "attachment; file=\"" + responseBody + "\"");
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
//        		File newFile = new File("./temp.csv");
//		return newFile;
//		return filebyte;
	}

}
