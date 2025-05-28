package com.bai.euclsystem.models;

import com.bai.euclsystem.common.AbstractEntity;
import com.bai.euclsystem.enums.ETokenStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "purchased_tokens")
public class PurchasedToken extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;


    @Column(unique = true, length = 16,nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private ETokenStatus status = ETokenStatus.NEW;

    private int tokenValueDays;

    private int amount;

    private LocalDateTime purchaseDate = LocalDateTime.now();
    private LocalDateTime expirationDate;
}
