package com.sap.gs.techcounsil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.gs.techcounsil.model.Developer;
import com.sap.gs.techcounsil.repository.DeveloperRepository;

@Service
public class DeveloperService {

	private DeveloperRepository developerRepository;

	@Autowired
	public DeveloperService(DeveloperRepository developerRepository) {
		this.developerRepository = developerRepository;
	}

	public Developer createDeveloper(Developer developer) {
		if (!isValidDeveloper(developer))
			throw new IllegalArgumentException("Invalid developer, please review information");

		return developerRepository.save(developer);
	}

	private boolean isValidDeveloper(Developer developer) {
		return developer != null && developer.getName() != null && !developer.getName().isEmpty()
				&& developer.getAge() != null;
	}

	public Iterable<Developer> getAllDevelopers() {
		return developerRepository.findAll();

	}

}
