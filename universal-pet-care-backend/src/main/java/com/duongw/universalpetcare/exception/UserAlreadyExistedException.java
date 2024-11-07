package com.duongw.universalpetcare.exception;


public class UserAlreadyExistedException extends RuntimeException {
    private String message;

    public UserAlreadyExistedException( String message1) {

        this.message = message1;
    }
}
