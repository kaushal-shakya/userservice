package com.kaushal.userservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class CommonModel {

    @Id
    @GeneratedValue( generator = "uuidgenerator", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuidgenerator", strategy = "uuid2")
    private UUID uuid;
}
