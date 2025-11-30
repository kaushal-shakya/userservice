package com.kaushal.userservice.service;

import com.kaushal.userservice.dtos.LoginRequestDto;
import com.kaushal.userservice.dtos.LoginResponseDto;
import com.kaushal.userservice.dtos.RegisteredUserDto;
import com.kaushal.userservice.dtos.SignupUserRequestDTO;
import com.kaushal.userservice.models.CommonModel;

import java.util.List;

public interface UserService {

    CommonModel signupNewUser(SignupUserRequestDTO requestDTO);
    LoginResponseDto login(LoginRequestDto requestDto);
    List<RegisteredUserDto> getAllUsers();
}
