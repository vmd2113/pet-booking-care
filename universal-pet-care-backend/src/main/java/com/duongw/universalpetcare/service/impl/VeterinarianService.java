package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.dto.EntityConverter;
import com.duongw.universalpetcare.dto.response.UserDTO;
import com.duongw.universalpetcare.exception.NotFoundException;
import com.duongw.universalpetcare.model.Appointment;
import com.duongw.universalpetcare.model.Veterinarian;
import com.duongw.universalpetcare.repository.AppointmentRepository;
import com.duongw.universalpetcare.repository.ReviewRepository;
import com.duongw.universalpetcare.repository.UserRepository;
import com.duongw.universalpetcare.repository.VeterinarianRepository;
import com.duongw.universalpetcare.service.IVeterinarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class VeterinarianService implements IVeterinarianService {


    private final EntityConverter<Veterinarian, UserDTO> entityConverter;
    private final VeterinarianRepository veterinarianRepository;
    private final PhotoService photoService;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;



    /**
     * Maps a Veterinarian object to a UserDTO object. The UserDTO is populated
     * with the Veterinarian's information, including their average rating and
     * the total number of reviewers. If the Veterinarian has a photo associated
     * with them, the photo is also included in the UserDTO.
     *
     * @param veterinarian the Veterinarian to be mapped
     * @return a UserDTO representing the Veterinarian
     */

    private UserDTO mapVeterinarianToUserDto(Veterinarian veterinarian) {
        UserDTO userDto = entityConverter.mapEntityToDTO(veterinarian, UserDTO.class);
        double averageRating = reviewService.getAverageRattingForVet(veterinarian.getId());
        userDto.setAverageRating(averageRating);
        Long totalReviewer = reviewRepository.countByVeterinarianId(veterinarian.getId());
        userDto.setTotalReviewers(totalReviewer);

        if (veterinarian.getPhoto() != null) {
            try {
                byte[] photoBytes = photoService.getImageData(veterinarian.getPhoto().getId());
                userDto.setPhoto(photoBytes);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return userDto;
    }


    /**
     * Gets all veterinarians from the database, and maps each one to a {@link UserDTO}
     * object which contains the average rating and the total number of reviewers
     * for each veterinarian.
     *
     * @return a list of {@link UserDTO} objects representing all veterinarians
     */

    @Override
    public List<UserDTO> getAllVeterinariansWithDetails() {
        List<Veterinarian> veterinarians = userRepository.findAllUsersByUserType("VET");
        return veterinarians.stream().map(this::mapVeterinarianToUserDto).toList();

    }


    /**
     * Determines if the given appointment overlaps with the given requested time.
     * <p>
     * Two appointments are considered to overlap if the start time of the first appointment is
     * before the end time of the second appointment, and the end time of the first appointment is
     * after the start time of the second appointment. Additionally, a buffer of 1 hour before and
     * 170 minutes after the appointment is also considered as overlap.
     *
     * @param appointment the appointment to check for overlap
     * @param requestedStartTime the start time of the requested appointment
     * @param requestedEndTime the end time of the requested appointment
     * @return true if the appointment overlaps with the requested time, false if not
     */

    public boolean doesAppointmentOverlap(Appointment appointment, LocalTime requestedStartTime, LocalTime requestedEndTime) {
        LocalTime appointmentStartTime = appointment.getAppointmentTime();
        LocalTime appointmentEndTime = appointment.getAppointmentTime().plusHours(2);

        LocalTime unavailableStartTime = appointmentStartTime.minusHours(1);
        LocalTime unavailableEndTime = appointmentEndTime.plusMinutes(170);
        return !requestedStartTime.isBefore(unavailableStartTime) && !requestedEndTime.isAfter(unavailableEndTime);

    }

    public boolean isVeterinarianAvailable(Veterinarian veterinarian, LocalDate requestedDate, LocalTime requestedTime) {
        if (requestedDate != null && requestedTime != null) {
            LocalTime requestedEndTime = requestedTime.plusHours(2);
            return appointmentRepository.findByVeterinarianAndAppointmentDate(veterinarian, requestedDate)
                    .stream()
                    .noneMatch(appointment -> doesAppointmentOverlap(appointment, requestedTime, requestedEndTime));
        }
        return true;
    }

    @Override
    public List<Veterinarian> getVeterinariansBySpecialization(String specialization) {
        List<Veterinarian> veterinarianList = veterinarianRepository.findAllBySpecialization(specialization);
        if(!veterinarianRepository.existsBySpecialization(specialization)){
            throw new NotFoundException("Specialization not found");
        }
        return veterinarianList;
    }

    private List<Veterinarian> getAvailableVeterinarians(String specialization, LocalDate date, LocalTime time) {
        List<Veterinarian> veterinarians = getVeterinariansBySpecialization(specialization);
        return veterinarians.stream()
                .filter(vet -> isVeterinarianAvailable(vet, date, time))
                .toList();

    }


    /**
     * Returns a list of UserDTO objects representing available veterinarians that match the given specialization.
     * The availability of the veterinarians is determined by the given date and time.
     * @param specialization the specialization of the veterinarians to search for
     * @param date the date of the appointment
     * @param time the time of the appointment
     * @return a list of UserDTO objects representing available veterinarians
     */

    @Override
    public List<UserDTO> findAvailableVetsForAppointment(String specialization, LocalDate date, LocalTime time) {
        List<Veterinarian> filteredVets = getAvailableVeterinarians(specialization, date, time);
        return filteredVets.stream()
                .map(this::mapVeterinarianToUserDto)
                .toList();
    }

    @Override
    public List<String> getSpecializations() {
        return veterinarianRepository.getSpecializations();
    }

    @Override
    public List<Map<String, Object>> aggregateVetsBySpecialization() {
        List<Object[]> results = veterinarianRepository.countVetsBySpecialization();
        return results.stream()
                .map(result -> Map.of("specialization", result[0], "count", result[1]))
                .collect(Collectors.toList());
    }


}
