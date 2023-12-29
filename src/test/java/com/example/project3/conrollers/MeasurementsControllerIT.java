package com.example.project3.conrollers;

import com.example.project3.dto.MeasurementDTO;
import com.example.project3.models.Measurement;
import com.example.project3.models.Sensor;
import com.example.project3.repositories.MeasurementRepository;
import com.example.project3.repositories.SensorRepository;
import com.example.project3.services.MeasurementService;
import com.example.project3.util.MeasurementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MeasurementsControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    MeasurementValidator measurementValidator;
    @Autowired
    MeasurementService measurementService;
    @MockBean
    MeasurementRepository measurementRepository;
    @MockBean
    SensorRepository sensorRepository;

    @Test
    void add_MeasurementDtoValid_ValidRequest_ReturnsValidResponseEntity() throws Exception {
        when(sensorRepository.findByName("someSensor")).thenReturn(Optional.of(new Sensor()));

        mockMvc.perform(post("/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "value": 10,
                        "raining": true,
                        "sensor": {
                            "name": "someSensor"
                            }
                        }
                        """)).andExpect(status().isOk());
        verify(sensorRepository, times(2)).findByName("someSensor");
        verify(measurementRepository, times(1)).save(any(Measurement.class));
    }

    @Test
    void add_MeasurementDtoValid_SensorNotFoundInBd_ReturnsInvalidResponseEntity() throws Exception {
        when(sensorRepository.findByName("someSensor")).thenReturn(Optional.empty());

        mockMvc.perform(post("/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "value": 10,
                        "raining": true,
                        "sensor": {
                            "name": "someSensor"
                            }
                        }
                        """)).andExpect(status().isBadRequest());
        verify(sensorRepository, times(1)).findByName("someSensor");
        verify(measurementRepository, times(0)).save(any(Measurement.class));
    }

    @Test
    void add_MeasurementDtoValid_InvalidRequest_ReturnsInvalidResponseEntity() throws Exception {
        when(sensorRepository.findByName("someSensor")).thenReturn(Optional.of(new Sensor()));

        mockMvc.perform(post("/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "value": 101,
                        "raining": true,
                        "sensor": {
                            "name": "someSensor"
                            }
                        }
                        """)).andExpect(status().isBadRequest());
        verify(sensorRepository, times(1)).findByName("someSensor");
        verify(measurementRepository, times(0)).save(any(Measurement.class));
    }

    @Test
    void getMeasurements_ReturnList() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setValue(10);

        when(measurementRepository.findAll()).thenReturn(List.of(measurement));

        mockMvc.perform(get("/measurements"))
                .andExpectAll(content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "value": 10,
                                        "raining": false,
                                        "sensor": null
                                    }
                                ]
                                """));
    }

    @Test
    void rainyDaysCountTest() throws Exception {
        when(measurementRepository.findByRaining(true)).thenReturn(List.of(new Measurement()));

        mockMvc.perform(get("/measurements/rainyDaysCount"))
                .andExpectAll(status().isOk(),
                        content().bytes("Количество дождливых дней = 1".getBytes()));
    }
}