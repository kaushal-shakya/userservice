package com.kaushal.userservice.service;

import com.kaushal.userservice.dtos.LoginRequestDto;
import com.kaushal.userservice.dtos.LoginResponseDto;
import com.kaushal.userservice.dtos.RegisteredUserDto;
import com.kaushal.userservice.dtos.SignupUserRequestDTO;
import com.kaushal.userservice.models.Account;
import com.kaushal.userservice.models.CommonModel;
import com.kaushal.userservice.repositories.AccountRepository;
import com.kaushal.userservice.security.AuthUtil;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService
{

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final PasswordEncoder encoder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserServiceImpl(AccountRepository accountRepository, AuthenticationManager authenticationManager, AuthUtil authUtil, PasswordEncoder encoder,
                           KafkaTemplate<String, String> kafkaTemplate)
    {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
        this.encoder = encoder;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public CommonModel signupNewUser(SignupUserRequestDTO requestDTO) {
        Account account = new Account();
        account.setEmail(requestDTO.getEmail());
        account.setPassword(encoder.encode(requestDTO.getPassword()));
        account.setFirstName(requestDTO.getFirstName());
        account.setLastName(requestDTO.getLastName());
        account.setCreatedAt(LocalDateTime.now());
        account.setModifiedAt(LocalDateTime.now());

        Account new_acc = accountRepository.save(account);
        CommonModel response = new CommonModel();
        response.setId(new_acc.getId());

        double i = Math.random();
        String message = "Message no : " + i;

        kafkaTemplate.send("newaccounttopic", "1", message );
        return response;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()
                ));

        Account account = (Account) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(account);

        return new LoginResponseDto(account.getId().toString(), token);
    }

    @Override
    public List<RegisteredUserDto> getAllUsers() {
        List<RegisteredUserDto> users = accountRepository.findAll().stream().map(account -> {
            RegisteredUserDto dto = new RegisteredUserDto();
            dto.setUuid(account.getId().toString());
            dto.setEmail(account.getEmail());
            dto.setFirstName(account.getFirstName());
            dto.setLastName(account.getLastName());
            dto.setCreatedAt(account.getCreatedAt());
            dto.setModifiedAt(account.getModifiedAt());
            return dto;
        }).toList();
        return users;
    }
}
