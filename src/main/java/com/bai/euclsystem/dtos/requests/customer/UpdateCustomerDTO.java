package com.bai.euclsystem.dtos.requests.customer;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCustomerDTO {

    private String firstName;

    private String lastName;

    @Email(message = "Invalid email")
    private String email;

    private String phoneNumber;


}