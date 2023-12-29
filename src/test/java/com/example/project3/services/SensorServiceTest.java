package com.example.project3.services;

import com.example.project3.models.Sensor;
import com.example.project3.repositories.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorService sensorService;

    Sensor createTestSensor() {
        Sensor sensor = new Sensor();
        sensor.setName("someSensor");
        return sensor;
    }

    @Test
    void findOne() {
        Sensor sensor = createTestSensor();
        when(sensorRepository.findByName("someSensor")).thenReturn(Optional.of(sensor));
        var result = sensorService.findOne("someSensor");
        assertNotNull(result.get());
        assertEquals(result.get(), sensor);
        verify(sensorRepository, times(1)).findByName("someSensor");
    }

    @Test
    void save() {
        sensorService.save(createTestSensor());
        verify(sensorRepository, times(1)).save(any());
    }
}
