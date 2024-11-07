package com.duongw.universalpetcare.dto.response;

import com.duongw.universalpetcare.enums.AppointmentStatus;
import com.duongw.universalpetcare.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
public class AppointmentDTO {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDate createdAt;
    private String reason;
    private AppointmentStatus status;
    private String appointmentNo;
    private PatientDTO patient;
    private VeterinarianDTO veterinarian;
    private List<PetDTO> pets;
}
