package com.example.project3.services;

import com.example.project3.models.Measurement;
import com.example.project3.models.Sensor;
import com.example.project3.repositories.MeasurementRepository;
import com.example.project3.repositories.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {

    @Mock
    SensorRepository sensorRepository;

    @Mock
    MeasurementRepository measurementRepository;

    @InjectMocks
    MeasurementService measurementService;

    Measurement createTestMeasurement() {
        Measurement measurement = new Measurement();
        Sensor sensor = new Sensor();
        sensor.setName("someSensor");
        measurement.setValue(10);
        measurement.setRaining(true);
        measurement.setSensor(sensor);
        return measurement;
    }

    @Test
    void findAll() {
        when(measurementRepository.findAll()).thenReturn(List.of(createTestMeasurement()));
        var result = measurementService.findAll();
        assertNotNull(result);
    }

    @Test
    void rainyDaysCount() {
        when(measurementRepository.findByRaining(true)).thenReturn(List.of(createTestMeasurement()));
        var result = measurementService.rainyDaysCount();
        assertEquals(1, result);
    }

    @Test
    void save() {
        Sensor sensor = new Sensor();
        sensor.setName("someSensor");
        when(sensorRepository.findByName("someSensor")).thenReturn(Optional.of(sensor));
        measurementService.save(createTestMeasurement());
        verify(sensorRepository, times(1)).findByName("someSensor");
        verify(measurementRepository, times(1)).save(any(Measurement.class));
    }
}