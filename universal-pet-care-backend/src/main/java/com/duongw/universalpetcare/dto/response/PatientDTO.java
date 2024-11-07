package com.duongw.universalpetcare.dto.response;

import lombok.Data;

@Data
public class PatientDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String phoneNumber;

}
