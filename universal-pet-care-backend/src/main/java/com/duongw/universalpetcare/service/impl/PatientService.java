package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.dto.EntityConverter;
import com.duongw.universalpetcare.dto.response.PatientDTO;
import com.duongw.universalpetcare.model.Patient;
import com.duongw.universalpetcare.repository.PatientRepository;
import com.duongw.universalpetcare.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PatientService implements IPatientService {

    private final PatientRepository patientRepository;

    private final EntityConverter<Patient, PatientDTO> entityConverter;



    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientDTO> patientDTOS = patients.stream().map(this::convertToDTO).toList();
        return patientDTOS;

    }

    public PatientDTO convertToDTO(Patient patient) {
        return entityConverter.mapEntityToDTO(patient, PatientDTO.class);
    }



}
