package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.dto.response.PatientDTO;
import com.duongw.universalpetcare.model.Patient;

import java.util.List;

public interface IPatientService {

    List<PatientDTO> getAllPatients();
}
