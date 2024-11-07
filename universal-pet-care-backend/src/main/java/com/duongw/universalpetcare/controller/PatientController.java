package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.dto.response.PatientDTO;
import com.duongw.universalpetcare.model.Patient;
import com.duongw.universalpetcare.service.IPatientService;
import com.duongw.universalpetcare.service.IPetService;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.PATIENTS_URL)
@RequiredArgsConstructor

@CrossOrigin("http://localhost:5173")
@Tag(name = "patients controller")
public class PatientController {

    private final IPatientService patientService;

    @GetMapping(path = "/all-patients")

    public ResponseEntity<ApiResponse> getAllPatients() {
        try {
            List<PatientDTO> patients = patientService.getAllPatients();
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Get all patients success", patients));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));


        }

    }
}
