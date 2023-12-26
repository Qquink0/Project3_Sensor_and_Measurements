package com.example.project3.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.internal.chartpart.Chart;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Consumer {

    private final static String sensorUrl = "http://localhost:8080/sensors/registration";
    private final static String measurementUrl = "http://localhost:8080/measurements/add";
    private final static String getMeasurementsUrl = "http://localhost:8080/measurements";
    private final static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {


//        Map<String, Object> jsonToSend = new HashMap<>();
//        jsonToSend.put("name", "testSensor");

//        HttpEntity<Map<String, Object>> request = null;

        // Регистрация нового сенсора
//        String result = restTemplate.postForObject(sensorUrl, request, String.class);
//        System.out.println(result);

//        Random random = new Random();
//
//        for (int i = 0; i < 1000; i++) {
//
//            Map<String, Object> jsonToSend = new HashMap<>();
//
//            Map<String, String> sensor = new HashMap<>();
//            sensor.put("name", "testSensor");
//
//            // Генерируем случайное значение от - 50 до 50
//            jsonToSend.put("value", String.valueOf(random.nextInt(101) - 50));
//            jsonToSend.put("raining", String.valueOf(random.nextBoolean()));
//            jsonToSend.put("sensor", sensor);
//
//
//            request = new HttpEntity<>(jsonToSend);
//
//            restTemplate.postForObject(measurementUrl, request, String.class);
//        }
        String result = restTemplate.getForObject(getMeasurementsUrl, String.class);

        double[] xData = new double[1000];
        double[] yData = new double[1000];

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(result);

            int i = 0;
            for (JsonNode node : jsonNode) {
                xData[i] = i;
                yData[i++] = node.get("value").asInt();
                if (i == 1000) break;
            }

            Chart chart = QuickChart.getChart("Temp Chart", "X", "Value", "y(x)", xData, yData);

            new SwingWrapper<>(chart).displayChart();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
