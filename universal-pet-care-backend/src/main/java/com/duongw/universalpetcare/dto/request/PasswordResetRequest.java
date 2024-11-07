package com.duongw.universalpetcare.dto.request;


import lombok.Data;



@Data

public class PasswordResetRequest {

    private String token;
    private String newPassword;



}
