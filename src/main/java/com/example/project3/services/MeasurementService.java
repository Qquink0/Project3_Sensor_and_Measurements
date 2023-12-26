package com.example.project3.services;

import com.example.project3.models.Measurement;
import com.example.project3.models.Sensor;
import com.example.project3.repositories.MeasurementRepository;
import com.example.project3.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public int rainyDaysCount() {
        return measurementRepository.findByRaining(true).size();
    }

    @Transactional
    public void save(Measurement measurement) {
        enrichMeasurement(measurement);

        Optional<Sensor> sensor = sensorRepository.findByName(measurement.getSensor().getName());

        measurement.setSensor(sensor.get());
        measurementRepository.save(measurement);
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setTime(LocalDateTime.now());
    }
}
