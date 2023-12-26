package com.example.project3;

import com.example.project3.models.Sensor;
import com.example.project3.repositories.SensorRepository;
import com.example.project3.services.SensorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorService sensorService;

    @Test
    public void testProcessData() {
        // Устанавливаем ожидаемое поведение для репозитория
        when(sensorRepository.findByName("sensor")).thenReturn(Optional.of(new Sensor()));

        // Вызываем метод сервиса
        Optional<Sensor> result = sensorService.findOne("sensor");

        // Проверяем, что результат не равен null
        assertNotNull(result);

        // Проверяем, был ли вызван метод репозитория
        verify(sensorRepository, times(1)).findByName("sensor");
    }
}
