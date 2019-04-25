package com.sap.gs.techcounsil.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sap.gs.techcounsil.model.Developer;
import com.sap.gs.techcounsil.service.DeveloperService;

@RestController("/v1/developer")
public class DeveloperController {
	private static Logger logger = LoggerFactory.getLogger(DeveloperController.class);

	private DeveloperService developerService;

	@Autowired
	public DeveloperController(DeveloperService developerService) {
		this.developerService = developerService;
	}

	@PostMapping(consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity createDeveloper(@RequestBody Developer developer) {
		logger.info("Request body is {}", developer);
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(developerService.createDeveloper(developer));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(createBadRequestResponse(e.getMessage()));
		}
	}

	@GetMapping
	public Iterable<Developer> getDevelopers() {
		return developerService.getAllDevelopers();
	}

	private String createBadRequestResponse(String message) {
		return "{ \"message\": \"" + message + "\"}";
	}

}
