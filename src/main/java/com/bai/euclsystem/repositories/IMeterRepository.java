package com.bai.euclsystem.repositories;

import com.bai.euclsystem.models.Meter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IMeterRepository extends JpaRepository<Meter , UUID> {

    Optional<Meter> findByMeterNumber(String meterNumber);
}
