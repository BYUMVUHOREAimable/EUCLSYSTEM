package com.bai.euclsystem.controllers;


import com.bai.euclsystem.dtos.requests.meter.CreateMeterDTO;
import com.bai.euclsystem.dtos.response.meter.MeterResponseDTO;
import com.bai.euclsystem.payload.ApiResponse;
import com.bai.euclsystem.services.IMeterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("meters")
@RequiredArgsConstructor
@Tag(name = "Meter")
public class MeterController {

    private final IMeterService meterService;



    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MeterResponseDTO>> generateMeter(@RequestBody CreateMeterDTO dto) {
        try{
            MeterResponseDTO response = meterService.createMeter(dto);
            return ApiResponse.success("Meter generated successfully", HttpStatus.CREATED, response);
        } catch (Exception e) {
            return ApiResponse.fail("Failed to generate meter", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
