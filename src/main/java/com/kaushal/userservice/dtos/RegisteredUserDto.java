package com.kaushal.userservice.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisteredUserDto {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
