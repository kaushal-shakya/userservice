package com.kaushal.userservice.error;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private HttpStatusCode statusCode;

    public ErrorResponse(String message, String details,HttpStatusCode statusCode) {
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
    }
}
