package com.bai.euclsystem.dtos.requests.meter;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateMeterDTO {
    private UUID customerId;
}
