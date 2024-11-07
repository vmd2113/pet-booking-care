package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.dto.response.UserDTO;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;

import com.duongw.universalpetcare.model.Veterinarian;
import com.duongw.universalpetcare.service.IVeterinarianService;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.duongw.universalpetcare.utils.UrlMapping.GET_ALL_VETERINARIANS_URL;

@RestController
@CrossOrigin("http://localhost:5173")

@RequestMapping(UrlMapping.VETERINARIANS_URL)
@RequiredArgsConstructor

@Tag(name = "veterinarians controller")
public class VeterinarianController {

    private final IVeterinarianService service;

    @GetMapping(path = GET_ALL_VETERINARIANS_URL)
    public ResponseEntity<ApiResponse> getAllVeterinarians() {
        try {
            List<UserDTO> veterinarians = service.getAllVeterinariansWithDetails();
            return ResponseEntity.ok(new ApiResponse(200, "Get all veterinarians success", veterinarians));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(500, e.getMessage()));
        }


    }

    @GetMapping(path = "/list")
    public ResponseEntity<ApiResponse> searchVeterinarians(@RequestParam String search) {
        try {
            List<UserDTO> veterinarians = service.getAllVeterinariansWithDetails();
            return ResponseEntity.ok(new ApiResponse(200, "Get all veterinarians success", veterinarians));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(500, e.getMessage()));
        }

    }

    @GetMapping(path = "/list-by-specialization")
    public ResponseEntity<ApiResponse> searchVeterinariansBySpecialization(@RequestParam String specialization) {
        try {
            List<Veterinarian> veterinarians = service.getVeterinariansBySpecialization(specialization);
            return ResponseEntity.ok(new ApiResponse(200, "Get all veterinarians success", veterinarians));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(500, e.getMessage()));
        }

    }


    @GetMapping(path = "/search-veterinarians")
    public ResponseEntity<ApiResponse> searchVeterinarianForAppointment(@RequestParam String specialization, @RequestParam(required = false) LocalDate date,
                                                                        @RequestParam(required = false) LocalTime time) {
        try {
            List<UserDTO> veterinarians = service.findAvailableVetsForAppointment(specialization, date, time);
            return ResponseEntity.ok(new ApiResponse(200, "Get all veterinarians success", veterinarians));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(500, e.getMessage()));
        }
    }

    @GetMapping(path = "/all-specializations")

    public ResponseEntity<ApiResponse> getAllSpecializations() {
        try {
            List<String> specializations = service.getSpecializations();
            return ResponseEntity.ok(new ApiResponse(200, "Get all specializations success", specializations));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(500, e.getMessage()));
        }
    }


    @GetMapping(path ="/get-by-specialization")
    public ResponseEntity<List<Map<String, Object>>> aggregateVetsBySpecialization(){
        List<Map<String, Object>> aggregatedVets = service.aggregateVetsBySpecialization();
        return ResponseEntity.ok(aggregatedVets);
    }





}
