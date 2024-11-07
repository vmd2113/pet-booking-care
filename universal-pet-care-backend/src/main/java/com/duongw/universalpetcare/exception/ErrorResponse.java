package com.duongw.universalpetcare.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponse implements Serializable {
    private Date timeStamp;
    private int statusCode;
    private String path;
    private String error;
    private String message;
}
