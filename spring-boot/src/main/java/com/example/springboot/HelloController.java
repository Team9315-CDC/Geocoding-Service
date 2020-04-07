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


@RestController
public class HelloController {

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping("/test")
	String test(@RequestParam("file") MultipartFile file) {
		// validate file
        if (file.isEmpty()) {
            System.out.println("Please select a CSV file to upload.");
        } else {
			try {
				System.out.println(file.getContentType());

				// TODO: stream csv file to BatchCurl.java for batch processing
				Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			} catch (Exception e) {
				System.out.println("exception occured: " + e.toString());
			}
		}
		return "{ \"result\": \"file of type " + file.getContentType() + " received\"}";
	}

}
