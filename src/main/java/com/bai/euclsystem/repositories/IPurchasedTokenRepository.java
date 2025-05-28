package com.bai.euclsystem.repositories;

import com.bai.euclsystem.models.PurchasedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IPurchasedTokenRepository extends JpaRepository<PurchasedToken, UUID> {

    Optional<PurchasedToken> findByToken(String token);
}
