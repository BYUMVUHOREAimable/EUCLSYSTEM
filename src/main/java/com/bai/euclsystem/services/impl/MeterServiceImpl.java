package com.bai.euclsystem.services.impl;

import com.bai.euclsystem.dtos.requests.meter.CreateMeterDTO;
import com.bai.euclsystem.dtos.response.meter.MeterResponseDTO;
import com.bai.euclsystem.exceptions.NotFoundException;
import com.bai.euclsystem.models.Customer;
import com.bai.euclsystem.models.Meter;
import com.bai.euclsystem.repositories.ICustomerRepository;
import com.bai.euclsystem.repositories.IMeterRepository;
import com.bai.euclsystem.services.IMeterService;
import com.bai.euclsystem.utils.helpers.TokenGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeterServiceImpl implements IMeterService {

    private final IMeterRepository meterRepository;
    private final ICustomerRepository customerRepository;
    private final TokenGeneratorUtil tokenGeneratorUtil;

    @Override
    public MeterResponseDTO createMeter(CreateMeterDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        String generateMeter = tokenGeneratorUtil.generateUniqueMeterNumber();

        Meter meter = Meter.builder()
                .meterNumber(generateMeter)
                .customer(customer)
                .build();
        meterRepository.save(meter);
        return MeterResponseDTO.builder()
                .id(meter.getId())
                .meterNumber(meter.getMeterNumber())
                .customerFullName(meter.getCustomer().getProfile().getFirstName() + " " + meter.getCustomer().getProfile().getLastName())
                .build();
    }
}
