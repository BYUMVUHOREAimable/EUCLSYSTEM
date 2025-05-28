package com.bai.euclsystem.repositories;

import com.bai.euclsystem.models.Customer;
import com.bai.euclsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ICustomerRepository extends JpaRepository<Customer, UUID> {


    boolean existsByProfile_Email(String email);

    Optional<Customer> findByProfile(User profile);

    Optional<Customer> findByMeters_MeterNumber(String metersMeterNumber);

}
