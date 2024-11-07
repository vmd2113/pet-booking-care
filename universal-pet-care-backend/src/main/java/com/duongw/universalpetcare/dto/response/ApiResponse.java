package com.duongw.universalpetcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

public class ApiResponse {
    private Integer codeStatus;
    private String message;
    private Object data;

    public ApiResponse(Integer codeStatus, String message){
        this.codeStatus = codeStatus;
        this.message = message;
    }



}
