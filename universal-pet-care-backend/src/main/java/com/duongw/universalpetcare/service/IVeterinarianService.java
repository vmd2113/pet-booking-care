package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.dto.response.UserDTO;
import com.duongw.universalpetcare.model.Veterinarian;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


public interface IVeterinarianService {




    List<UserDTO> getAllVeterinariansWithDetails();

    List<Veterinarian> getVeterinariansBySpecialization(String specialization);

    List<UserDTO> findAvailableVetsForAppointment(String specialization, LocalDate date, LocalTime time);

    List<String> getSpecializations();

    List<Map<String, Object>> aggregateVetsBySpecialization();


}
