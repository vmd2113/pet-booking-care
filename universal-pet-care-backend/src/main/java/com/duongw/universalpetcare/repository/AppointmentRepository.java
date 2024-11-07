package com.duongw.universalpetcare.repository;

import com.duongw.universalpetcare.enums.AppointmentStatus;
import com.duongw.universalpetcare.model.Appointment;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Appointment findByAppointmentNo(String appointmentNo);
    boolean existsByVeterinarianIdAndPatientIdAndStatus(Long veterinarianId,
                                                        Long reviewerId,
                                                        AppointmentStatus appointmentStatus);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id =:userId OR a.veterinarian.id =:userId ")
    List<Appointment> findAllByUserId(@Param("userId") Long userId);



    List<Appointment> findByVeterinarianAndAppointmentDate(User veretinarianUser, LocalDate requestedDate);

    List<Appointment> findAllByAppointmentDate(LocalDate requestedDate);



    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment>findAllByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);
}

