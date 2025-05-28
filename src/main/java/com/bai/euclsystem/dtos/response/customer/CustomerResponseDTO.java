package com.bai.euclsystem.dtos.response.customer;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerResponseDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean verified;



}