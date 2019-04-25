package com.sap.gs.techcounsil.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	private static final String API_VERSION = "/v1/";

	private static final String HELLO_WORLD_PATH = "helloWorld";

	@GetMapping(path = API_VERSION + HELLO_WORLD_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String helloWorld() {
		return "{\"hello\": \"world\"}";
	}

}
