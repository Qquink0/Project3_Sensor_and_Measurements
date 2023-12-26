package com.example.project3;

import com.example.project3.repositories.MeasurementRepository;
import com.example.project3.repositories.SensorRepository;
import com.example.project3.services.MeasurementService;
import com.example.project3.services.SensorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootTest
class Project3Tests {

	@Autowired
	private MeasurementService measurementService;
	@Autowired
	private SensorService sensorService;
	@Autowired
	private MeasurementRepository measurementRepository;
	@Autowired
	private SensorRepository sensorRepository;
	@Test
	void contextLoads() {
		Assertions.assertNotNull(measurementService);
		Assertions.assertNotNull(sensorService);
		Assertions.assertNotNull(measurementRepository);
		Assertions.assertNotNull(sensorRepository);
	}

}
