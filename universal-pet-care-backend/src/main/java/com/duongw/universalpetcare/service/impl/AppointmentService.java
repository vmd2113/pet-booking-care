package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.dto.EntityConverter;
import com.duongw.universalpetcare.dto.request.AppointmentUpdateRequest;
import com.duongw.universalpetcare.dto.request.BookAppointmentRequest;
import com.duongw.universalpetcare.dto.response.AppointmentDTO;
import com.duongw.universalpetcare.dto.response.PetDTO;
import com.duongw.universalpetcare.enums.AppointmentStatus;
import com.duongw.universalpetcare.exception.InvalidDataException;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.*;
import com.duongw.universalpetcare.repository.AppointmentRepository;
import com.duongw.universalpetcare.repository.UserRepository;
import com.duongw.universalpetcare.service.IAppointmentService;

import com.duongw.universalpetcare.service.IPetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final IPetService petService;

    private final EntityConverter<Appointment, AppointmentDTO> entityConverter;
    private final EntityConverter<Pet, PetDTO> petEntityConverter;


    @Transactional
    @Override
    public Appointment createAppointment(BookAppointmentRequest request, Long senderId, Long recipientId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", senderId.toString()));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", recipientId.toString()));

        if (sender != null && recipient != null) {
            if (recipient instanceof Veterinarian && sender instanceof Patient) {
                Appointment appointment = request.getAppointment();
                List<Pet> pets = request.getPets();
                pets.forEach(pet -> pet.setAppointment(appointment));
                List<Pet> savedPets = petService.savePetForAppointment(pets);
                appointment.setPets(savedPets);

                appointment.addPatient(sender);
                appointment.addVeterinarian(recipient);
                appointment.setAppointmentNo();
                appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
                return appointmentRepository.save(appointment);
            }
            else{
                throw new InvalidDataException("Invalid data");
            }

        }
        throw new ResourceNotFoundException("Something", "was", "wrong");
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentUpdateRequest request) {
        Appointment existingAppointment = getAppointmentById(id);
        if (!Objects.equals(existingAppointment.getStatus(), AppointmentStatus.WAITING_FOR_APPROVAL)) {
            throw new IllegalStateException("Sorry, this appointment is already approved");
        }

        existingAppointment.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
        existingAppointment.setAppointmentTime(LocalTime.parse(request.getAppointmentTime()));
        existingAppointment.setReason(request.getReason());
        return appointmentRepository.save(existingAppointment);
    }


    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.findById(id)
                .ifPresentOrElse(appointmentRepository::delete, () -> {
                    throw new ResourceNotFoundException("Appointment", "Id", id.toString());
                });

    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "Id", id.toString()));
    }

    @Override
    public Appointment getAppointmentByNo(String appointmentNo) {
        return appointmentRepository.findByAppointmentNo(appointmentNo);
    }

    @Override
    public List<AppointmentDTO> getUserAppointments(Long userId) {
        List<Appointment> appointments = appointmentRepository.findAllByUserId(userId);
        return appointments.stream().map(appointment ->
        {
            AppointmentDTO appointmentDTO = entityConverter.mapEntityToDTO(appointment, AppointmentDTO.class);
            List<PetDTO> petDto = appointment.getPets()
                    .stream()
                    .map(pet -> petEntityConverter.mapEntityToDTO(pet, PetDTO.class)).toList();
            appointmentDTO.setPets(petDto);
            return appointmentDTO;

        }).toList();

    }

    @Override
    public Appointment cancelAppointment(Long id) {
        return appointmentRepository.findById(id)
                .filter(appointment -> appointment.getStatus().equals(AppointmentStatus.WAITING_FOR_APPROVAL))
                .map(appointment -> {appointment.setStatus(AppointmentStatus.CANCELLED);
                    return appointmentRepository.saveAndFlush(appointment);
                }).orElseThrow(() -> new IllegalStateException("Something went wrong, cannot cancel this appointment"));

    }

//    @Override
//    public  Appointment approveAppointment(Long appointmentId) {
//        return appointmentRepository.findById(appointmentId)
//                .filter(appointment -> appointment.getStatus().equals(AppointmentStatus.WAITING_FOR_APPROVAL))
//                .map(appointment -> {appointment.setStatus(AppointmentStatus.APPROVED);
//                    return appointmentRepository.saveAndFlush(appointment);
//                }).orElseThrow(() -> new IllegalStateException("Something went wrong, cannot approve this appointment"));
//
//    }

    @Override
    public Appointment approveAppointment(Long appointmentId) {
        // Tìm kiếm cuộc hẹn theo ID
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();

            // Kiểm tra xem trạng thái hiện tại có phải là WAITING_FOR_APPROVAL không
            if (appointment.getStatus().equals(AppointmentStatus.WAITING_FOR_APPROVAL)) {
                // Đặt lại trạng thái thành APPROVED và lưu thay đổi
                appointment.setStatus(AppointmentStatus.APPROVED);
                return appointmentRepository.saveAndFlush(appointment);
            } else {
                // Nếu trạng thái không hợp lệ, ném ngoại lệ
                throw new IllegalStateException("Something went wrong, cannot approve this appointment");
            }
        } else {
            // Nếu không tìm thấy cuộc hẹn, ném ngoại lệ
            throw new ResourceNotFoundException("Appointment", "id", appointmentId.toString());
        }
    }


    @Override
    public  Appointment declineAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .filter(appointment -> appointment.getStatus().equals(AppointmentStatus.WAITING_FOR_APPROVAL))
                .map(appointment -> {appointment.setStatus(AppointmentStatus.NOT_APPROVED);
                    return appointmentRepository.saveAndFlush(appointment);
                }).orElseThrow(() -> new IllegalStateException("Something went wrong, cannot decline this appointment"));

    }

    @Override
    public void deleteAll(List<Appointment> appointments) {
        appointmentRepository.deleteAll(appointments);
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findAllByAppointmentDate(date).stream().map(appointment -> entityConverter.mapEntityToDTO(appointment, AppointmentDTO.class)).toList();
    }


    @Override
    public List<AppointmentDTO> getAllAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return appointmentRepository.findAllByAppointmentDateBetween(startDate, endDate).stream().map(appointment -> entityConverter.mapEntityToDTO(appointment, AppointmentDTO.class)).toList();
    }

    @Override
    public List<Appointment> findAllAppointmentsByUserId(Long userId) {
        return appointmentRepository.findAllByUserId(userId);
    }


    @Override

    public long countAllAppointments() {
        return appointmentRepository.count();
    }


    @Override
    public List<Map<String, Object>> getAppointmentSummary() {
        return getAllAppointments()
                .stream()
                .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> createStatusSummaryMap(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }



    private Map<String, Object> createStatusSummaryMap(AppointmentStatus status, Long value){
        Map<String, Object> summaryMap = new HashMap<>();
        summaryMap.put("name", formatAppointmentStatus(status));
        summaryMap.put("value", value);
        return summaryMap;
    }


    private String formatAppointmentStatus(AppointmentStatus appointmentStatus) {
        return appointmentStatus.toString().replace("_", "-").toLowerCase();
    }


    @Override
    public List<Long> getAppointmentIds() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return  appointments.stream()
                .map(Appointment::getId)
                .collect(Collectors.toList());
    }
    @Override
    public void setAppointmentStatus(Long appointmentId){
        Appointment appointment = getAppointmentById(appointmentId);
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime appointmentEndTime = appointment.getAppointmentTime()
                .plusMinutes(2).truncatedTo(ChronoUnit.MINUTES);

        switch (appointment.getStatus()) {
            case APPROVED:
                if (currentDate.isBefore(appointment.getAppointmentDate()) ||
                        (currentDate.equals(appointment.getAppointmentDate()) && currentTime.isBefore(appointment.getAppointmentTime()))) {
                    appointment.setStatus(AppointmentStatus.UP_COMING);
                    // If already UP_COMING, no change needed.
                }
                break;

            case UP_COMING:
                if (currentDate.equals(appointment.getAppointmentDate()) &&
                        currentTime.isAfter(appointment.getAppointmentTime()) && !currentTime.isAfter(appointmentEndTime)) {
                    // Changed to include the end time as part of ON_GOING status
                    appointment.setStatus(AppointmentStatus.ON_GOING);
                }
                break;
            case ON_GOING:
                if (currentDate.isAfter(appointment.getAppointmentDate()) ||
                        (currentDate.equals(appointment.getAppointmentDate()) && !currentTime.isBefore(appointmentEndTime))) {
                    // Changed to mark as COMPLETED when current time is not before the end time
                    appointment.setStatus(AppointmentStatus.COMPLETED);
                }
                break;

            case WAITING_FOR_APPROVAL:
                if (currentDate.isAfter(appointment.getAppointmentDate()) ||
                        (currentDate.equals(appointment.getAppointmentDate()) && currentTime.isAfter(appointment.getAppointmentTime()))) {
                    // Adjusted to change status to NOT_APPROVED if current time is past the appointment time
                    appointment.setStatus(AppointmentStatus.NOT_APPROVED);
                }
                break;
        }
        appointmentRepository.save(appointment);

    }




}

