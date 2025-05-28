package com.bai.euclsystem.dtos.response.token;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PurchaseTokenResponseDTO {

    private String token;
    private int tokenValueInDays;
    private String meterNumber;
    private LocalDateTime purchaseDate;
    private int amount;
}
