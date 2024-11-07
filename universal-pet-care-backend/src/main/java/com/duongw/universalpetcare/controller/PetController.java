package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.Pet;
import com.duongw.universalpetcare.service.IPetService;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMapping.PETS_URL)

@CrossOrigin("http://localhost:5173")

@Tag(name = "pet controller")

public class PetController {

    private final IPetService petService;

    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse> savePets(@RequestBody List<Pet> pets) {
        try {
            List<Pet> savedPets = petService.savePetForAppointment(pets);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "save pets success", savedPets));
        } catch (RuntimeException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> getPetById(@PathVariable(name = "id") Long petId) {
        try {
            Pet pet = petService.getPetById(petId);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "success", pet));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> deletePetById(@PathVariable(name = "id") Long petId) {
        try {
            petService.deletePet(petId);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "delete success"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));

        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updatePet(@PathVariable(name = "id") Long petId, @RequestBody Pet pet) {
        try {
            Pet thePet = petService.updatePet(pet, petId);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "Update success", thePet));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()
            ));
        }
    }

    @GetMapping(path = "/types")
    public ResponseEntity<ApiResponse> getAllPetType() {
        try {
            List<String> thePetTypes = petService.getPetTypes();
            return ResponseEntity.ok(new ApiResponse(OK.value(), "Update success", thePetTypes));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()
            ));
        }
    }

    @GetMapping(path = "/breeds")
    public ResponseEntity<ApiResponse> getAllPetBreed(@RequestParam String petType) {
        try {
            List<String> thePetTypes = petService.getPetBreed(petType);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "get all breed success", thePetTypes));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()
            ));
        }
    }


    @GetMapping(path = "/colors")
    public ResponseEntity<ApiResponse> getAllPetColor() {
        try {
            List<String> thePetTypes = petService.getPetColor();
            return ResponseEntity.ok(new ApiResponse(OK.value(), "Update success", thePetTypes));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()
            ));
        }
    }


}
