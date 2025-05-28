package com.bai.euclsystem.dtos.response.meter;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MeterResponseDTO {


    private UUID id;
    private String customerFullName;
    private String meterNumber;

}
