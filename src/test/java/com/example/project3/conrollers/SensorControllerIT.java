package com.example.project3.conrollers;

import com.example.project3.models.Sensor;
import com.example.project3.repositories.SensorRepository;
import com.example.project3.services.SensorService;
import com.example.project3.util.SensorValidator;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class SensorControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SensorService sensorService;
    @Autowired
    SensorValidator sensorValidator;
    @MockBean
    SensorRepository sensorRepository;
    @Autowired
    ModelMapper modelMapper;


    @Test
    void registrationSensor_ReturnsValidResponseEntity() throws Exception {

        when(sensorRepository.findByName("someSensor")).thenReturn(Optional.empty());

        mockMvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "someSensor"
                                }
                                """))
                .andExpect(status().isOk());
        verify(sensorRepository, times(1)).findByName("someSensor");
        verify(sensorRepository, times(1)).save(any(Sensor.class));
    }

    @Test
    void registrationSensor_SensorAlreadyInDB_ReturnsInvalidResponseEntity() throws Exception {

        when(sensorRepository.findByName("someSensor")).thenReturn(Optional.of(new Sensor()));

        mockMvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "someSensor"
                                }
                                """))
                .andExpectAll(status().isBadRequest());
        verify(sensorRepository, times(1)).findByName("someSensor");
    }

    @Test
    void registrationSensor_InvalidRequest_ReturnsInvalidResponseEntity() throws Exception {

        when(sensorRepository.findByName("")).thenReturn(Optional.empty());

        mockMvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": ""
                                }
                                """))
                .andExpectAll(status().isBadRequest());
        verify(sensorRepository, times(1)).findByName("");
    }
}
