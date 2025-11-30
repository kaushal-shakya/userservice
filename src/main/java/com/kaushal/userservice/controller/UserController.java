package com.kaushal.userservice.controller;

import com.kaushal.userservice.dtos.SignupUserRequestDTO;
import com.kaushal.userservice.models.CommonModel;
import com.kaushal.userservice.dtos.LoginRequestDto;
import com.kaushal.userservice.dtos.LoginResponseDto;
import com.kaushal.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonModel> signup(@RequestBody SignupUserRequestDTO signUpRequest) {
        CommonModel response = userService.signupNewUser(signUpRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
