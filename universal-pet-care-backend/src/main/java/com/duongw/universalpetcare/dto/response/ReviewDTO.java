package com.duongw.universalpetcare.dto.response;

import lombok.Data;

@Data
public class ReviewDTO {

    private Long id;
    private int stars;
    private String feedback;
    private Long veterinarianId;
    private String veterinarianName;
    private Long patientId;
    private String patientName;
    private byte[] patientImage;
    private byte[] veterinarianImage;
}
