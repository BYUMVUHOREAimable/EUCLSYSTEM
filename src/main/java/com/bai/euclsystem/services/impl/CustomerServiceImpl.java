package com.bai.euclsystem.services.impl;

import com.bai.euclsystem.dtos.requests.customer.CreateCustomerDTO;
import com.bai.euclsystem.dtos.requests.customer.UpdateCustomerDTO;
import com.bai.euclsystem.dtos.response.customer.CustomerResponseDTO;
import com.bai.euclsystem.enums.ERole;
import com.bai.euclsystem.exceptions.AppException;
import com.bai.euclsystem.exceptions.BadRequestException;
import com.bai.euclsystem.exceptions.NotFoundException;
import com.bai.euclsystem.models.Customer;
import com.bai.euclsystem.models.Role;
import com.bai.euclsystem.models.User;
import com.bai.euclsystem.repositories.ICustomerRepository;
import com.bai.euclsystem.repositories.IUserRepository;
import com.bai.euclsystem.services.ICustomerService;
import com.bai.euclsystem.services.IRoleService;
import com.bai.euclsystem.utils.helpers.CustomerHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepository customerRepository;
    private final IUserRepository userRepository;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerHelper customerHelper;

    @Override
    public CustomerResponseDTO createCustomer(CreateCustomerDTO dto) {
        if(customerRepository.existsByProfile_Email(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        try{
            Role role = roleService.getRoleByName(ERole.CUSTOMER);

            User user = customerHelper.buildUserFromDto(dto, role, passwordEncoder);
            user = userRepository.save(user);

            Customer customer = customerHelper.buildCustomer(user);
            customerRepository.save(customer);

            CustomerResponseDTO response = CustomerResponseDTO.builder()
                    .id(customer.getId())
                    .fullName(customer.getProfile().getFirstName() + " " + customer.getProfile().getLastName())
                    .email(customer.getProfile().getEmail())
                    .phoneNumber(customer.getProfile().getPhoneNumber())
                    .verified(customer.getProfile().isVerified())
                    .build();
            return response;
        } catch (Exception e) {
            throw new AppException("Failed to create customer: " + e.getMessage());
        }
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
    }

    @Override
    public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
        Page<CustomerResponseDTO> customers = customerRepository.findAll(pageable)
                .map(customer -> {
                    CustomerResponseDTO response = CustomerResponseDTO
                            .builder()
                            .id(customer.getId())
                            .fullName(customer.getProfile().getFirstName() + " " + customer.getProfile().getLastName())
                            .email(customer.getProfile().getEmail())
                            .verified(customer.getProfile().isVerified())
                            .phoneNumber(customer.getProfile().getPhoneNumber())
                            .build();

                    return response;
                });
        return customers;
    }

    @Override
    public Customer updateCustomer(UUID customerId, UpdateCustomerDTO dto) {
        Customer customer = getCustomerById(customerId);
        User profile = customer.getProfile();
        if(dto.getFirstName() != null){
            profile.setFirstName(dto.getFirstName());
        }

        if(dto.getLastName() != null){
            profile.setLastName(dto.getLastName());
        }

        if(dto.getPhoneNumber() != null){
            profile.setPhoneNumber(dto.getPhoneNumber());
        }

        if(dto.getEmail() != null){
            profile.setEmail(dto.getEmail());
        }

        if(dto.getEmail() != null){
            profile.setEmail(dto.getEmail());
        }

        userRepository.save(profile);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(UUID customerId) {
        Customer customer = getCustomerById(customerId);
        userRepository.delete(customer.getProfile());
        customerRepository.delete(customer);
        try {
            customerRepository.flush();
        } catch (Exception e) {
            throw new AppException("Failed to delete customer: " + e.getMessage());
        }
    }
}
