package com.bai.euclsystem.dtos.requests.token;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PurchaseTokenRequestDTO {

    @NotBlank(message = "Meter number cannot be blank")
    @Size(min = 6, max = 6)
    private String meter;

    @Min(100)
    private int amount;
}
