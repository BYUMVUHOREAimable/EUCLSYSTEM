package com.bai.euclsystem.services;

import com.bai.euclsystem.dtos.requests.customer.CreateCustomerDTO;
import com.bai.euclsystem.dtos.requests.customer.UpdateCustomerDTO;
import com.bai.euclsystem.dtos.response.customer.CustomerResponseDTO;
import com.bai.euclsystem.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICustomerService {

    CustomerResponseDTO createCustomer(CreateCustomerDTO dto);

    Customer getCustomerById(UUID customerId);

    Page<CustomerResponseDTO> getAllCustomers(Pageable pageable);

    Customer updateCustomer(UUID customerId, UpdateCustomerDTO dto);

    void deleteCustomer(UUID customerId);
}