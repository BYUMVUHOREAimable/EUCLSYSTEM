package com.bai.euclsystem.services;

import com.bai.euclsystem.dtos.requests.token.PurchaseTokenRequestDTO;
import com.bai.euclsystem.dtos.response.token.PurchaseTokenResponseDTO;

public interface IPurchaseTokenService {

    PurchaseTokenResponseDTO purchaseToken(PurchaseTokenRequestDTO dto);
}
