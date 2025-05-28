package com.bai.euclsystem.controllers;

import com.bai.euclsystem.dtos.requests.customer.UpdateCustomerDTO;
import com.bai.euclsystem.dtos.response.customer.CustomerResponseDTO;
import com.bai.euclsystem.models.Customer;
import com.bai.euclsystem.payload.ApiResponse;
import com.bai.euclsystem.services.ICustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
@Tag(name = "Customer")
public class CustomerController {

    private final ICustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable UUID id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ApiResponse.success("Customer retrieved successfully", HttpStatus.OK, customer);
        } catch (Exception e) {
            return ApiResponse.fail("Customer not found", HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<CustomerResponseDTO>>> getAllCustomers(Pageable pageable) {
        try {
            Page<CustomerResponseDTO> customers = customerService.getAllCustomers(pageable);
            return ApiResponse.success("Customers retrieved successfully", HttpStatus.OK, customers);
        } catch (Exception e) {
            return ApiResponse.fail("Failed to retrieve customers", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerDTO dto
    ) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, dto);
            return ApiResponse.success("Customer updated successfully", HttpStatus.OK, updatedCustomer);
        } catch (Exception e) {
            return ApiResponse.fail("Failed to update customer", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable UUID id) {
        try {
            customerService.deleteCustomer(id);
            return ApiResponse.success("Customer deleted successfully", HttpStatus.NO_CONTENT, null);
        } catch (Exception e) {
            return ApiResponse.fail("Failed to delete customer", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
