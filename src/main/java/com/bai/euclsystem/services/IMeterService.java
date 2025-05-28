package com.bai.euclsystem.services;

import com.bai.euclsystem.dtos.requests.meter.CreateMeterDTO;
import com.bai.euclsystem.dtos.response.meter.MeterResponseDTO;

public interface IMeterService {

    MeterResponseDTO createMeter(CreateMeterDTO dto);
}
