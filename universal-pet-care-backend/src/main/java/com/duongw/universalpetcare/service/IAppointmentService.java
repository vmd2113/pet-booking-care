package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.dto.request.AppointmentUpdateRequest;
import com.duongw.universalpetcare.dto.response.AppointmentDTO;
import com.duongw.universalpetcare.model.Appointment;
import com.duongw.universalpetcare.dto.request.BookAppointmentRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IAppointmentService {
    Appointment createAppointment(BookAppointmentRequest request, Long senderId, Long recipientId);
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Long id, AppointmentUpdateRequest request);
    void deleteAppointment(Long id);
    Appointment getAppointmentById(Long id);
    Appointment getAppointmentByNo(String appointmentNo);
    List<AppointmentDTO> getUserAppointments(Long userId);

    Appointment cancelAppointment(Long id);
    Appointment approveAppointment(Long id);
    Appointment declineAppointment(Long id);

    void deleteAll(List<Appointment> appointments);

    List<AppointmentDTO> getAllAppointmentsByDate(LocalDate date);
    List<AppointmentDTO> getAllAppointmentsByDateRange(LocalDate startDate, LocalDate endDate);

    List<Appointment> findAllAppointmentsByUserId(Long userId);


    long countAllAppointments();

    List<Map<String, Object>> getAppointmentSummary();

    List<Long> getAppointmentIds();

    void setAppointmentStatus(Long appointmentId);
}
