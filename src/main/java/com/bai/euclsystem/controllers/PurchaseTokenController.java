package com.bai.euclsystem.controllers;


import com.bai.euclsystem.dtos.requests.token.PurchaseTokenRequestDTO;
import com.bai.euclsystem.dtos.response.token.PurchaseTokenResponseDTO;
import com.bai.euclsystem.payload.ApiResponse;
import com.bai.euclsystem.services.IPurchaseTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("purchase-tokens")
@RequiredArgsConstructor
@Tag(name = "Purchase Token")
public class PurchaseTokenController {

    private  final IPurchaseTokenService purchaseTokenService;


    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<PurchaseTokenResponseDTO>> purchaseToken(PurchaseTokenRequestDTO dto) {
        try{
            PurchaseTokenResponseDTO response = purchaseTokenService.purchaseToken(dto);
            return ApiResponse.success("Purchase token purchased successfully", HttpStatus.OK, response);
        } catch (Exception e) {
            return ApiResponse.fail("Failed to purchase token", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
