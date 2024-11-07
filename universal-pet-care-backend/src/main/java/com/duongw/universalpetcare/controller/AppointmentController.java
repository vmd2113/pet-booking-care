package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.request.AppointmentUpdateRequest;
import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.dto.response.AppointmentDTO;
import com.duongw.universalpetcare.event.AppointmentApprovedEvent;
import com.duongw.universalpetcare.event.AppointmentBookingEvent;
import com.duongw.universalpetcare.event.AppointmentDeclinedEvent;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.Appointment;
import com.duongw.universalpetcare.service.impl.AppointmentService;
import com.duongw.universalpetcare.dto.request.BookAppointmentRequest;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = UrlMapping.APPOINTMENTS_URL)
@Tag(name = "appointment controller")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class AppointmentController {

    private final  AppointmentService appointmentService;
    private final  ApplicationEventPublisher applicationEventPublisher;




    /**
     * Returns a list of all appointments.
     *
     * @return a list of appointments
     */
    @GetMapping(path = "/")
    public ResponseEntity<ApiResponse> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), "Get all appointments success", appointments));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<ApiResponse> bookAppointment(
            @RequestBody BookAppointmentRequest request,
            @RequestParam Long senderId,
            @RequestParam Long recipientId
    ) {

        try {
            Appointment theAppointment = appointmentService.createAppointment(request, senderId, recipientId);
            applicationEventPublisher.publishEvent(new AppointmentBookingEvent(theAppointment));
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "", theAppointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }


    }

    @GetMapping(path = "/by-id/{id}")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.status(OK).body(new ApiResponse(OK.value(), "get by Id success", appointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping(path = "/by-no/{appointmentNo}")
    public ResponseEntity<ApiResponse> getAppointmentByNo(@PathVariable String appointmentNo) {
        try {
            Appointment appointment = appointmentService.getAppointmentByNo(appointmentNo);
            return ResponseEntity.status(OK).body(new ApiResponse(OK.value(), "success", appointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAppointmentById(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "deleted success"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateRequest request) {
        try {
            Appointment appointment = appointmentService.updateAppointment(id, request);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "success", appointment));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(NOT_ACCEPTABLE.value(), e.getMessage()));
        }
    }

    @PutMapping("/cancel-appointment/{id}")
    public ResponseEntity<ApiResponse> cancelAppointment(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.cancelAppointment(id);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "success", appointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(NOT_ACCEPTABLE.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @PutMapping(path = "/approve-appointment/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse> approveAppointment(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.approveAppointment(id);
            applicationEventPublisher.publishEvent(new AppointmentApprovedEvent(appointment));
            return ResponseEntity.ok(new ApiResponse(OK.value(), "success", appointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(NOT_ACCEPTABLE.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @PutMapping(path = "/decline-appointment/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse> declineAppointment(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.declineAppointment(id);

            applicationEventPublisher.publishEvent(new AppointmentDeclinedEvent(appointment));
            return ResponseEntity.ok(new ApiResponse(OK.value(), "success", appointment));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(NOT_ACCEPTABLE.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/count-appointments")
    public ResponseEntity<ApiResponse> countAllAppointments() {
        try {
            Long count = appointmentService.countAllAppointments();
            return ResponseEntity.ok(new ApiResponse(OK.value(), "success", count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(NOT_ACCEPTABLE.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/summary/appointments-summary")
    public ResponseEntity<ApiResponse> getAppointmentSummary() {
        try {
            List<Map<String, Object>> summary = appointmentService.getAppointmentSummary();
            return ResponseEntity.ok(new ApiResponse(200, "Appointment summary retrieved successfully", summary));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(500, "Error retrieving appointment summary: " + e.getMessage(), null));
        }
    }


}
