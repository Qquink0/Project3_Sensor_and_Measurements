package com.example.project3.conrollers;

import com.example.project3.dto.MeasurementDTO;
import com.example.project3.models.Measurement;
import com.example.project3.services.MeasurementService;
import com.example.project3.util.MeasurementErrorResponse;
import com.example.project3.util.MeasurementValidator;
import com.example.project3.util.MeasurementsNotCreatedException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;
    private final MeasurementService measurementService;

    @Autowired
    public MeasurementsController(ModelMapper modelMapper, MeasurementValidator measurementValidator, MeasurementService measurementService) {
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
        this.measurementService = measurementService;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult) {
        Measurement measurement = convertToMeasurement(measurementDTO);

        if (measurement.getSensor() != null) measurementValidator.validate(measurement.getSensor(), bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new MeasurementsNotCreatedException(errorMsg.toString());
        }

        measurementService.save(measurement);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurements() {
        return measurementService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public String rainyDaysCount() {
        return "Количество дождливых дней = " + measurementService.rainyDaysCount();
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handlerException(MeasurementsNotCreatedException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
