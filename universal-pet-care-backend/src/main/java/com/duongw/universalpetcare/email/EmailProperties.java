package com.duongw.universalpetcare.email;

import lombok.Data;

@Data
public class EmailProperties {
    public static final String DEFAULT_HOST = "smtp.gmail.com";
    public static final int DEFAULT_PORT = 587;
    public static final String DEFAULT_SENDER = "[your_gmail]";
    public static final String DEFAULT_USERNAME = "[your_gmail]";
    public static final String DEFAULT_PASSWORD = "[your_application_password]";
    public static final boolean DEFAULT_AUTH = true;
    public static final boolean DEFAULT_STARTTLS = true;
}
