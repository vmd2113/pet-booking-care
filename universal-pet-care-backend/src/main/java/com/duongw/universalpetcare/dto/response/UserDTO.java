package com.duongw.universalpetcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    private  Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String userType;
    private boolean isEnabled;
    private String specialization;
    private LocalDate createdAt;
    private List<AppointmentDTO> appointments;
    private List<ReviewDTO> reviews;
    private long photoId;
    private byte[] photo;
    private double averageRating;
    private Long totalReviewers;
}
