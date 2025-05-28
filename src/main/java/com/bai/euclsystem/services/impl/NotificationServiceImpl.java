package com.bai.euclsystem.services.impl;

import com.bai.euclsystem.enums.IEmailTemplate;
import com.bai.euclsystem.models.Notification;
import com.bai.euclsystem.repositories.ICustomerRepository;
import com.bai.euclsystem.repositories.INotificationRepository;
import com.bai.euclsystem.repositories.IPurchasedTokenRepository;
import com.bai.euclsystem.services.INotificationService;
import com.bai.euclsystem.standalone.EmailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final IPurchasedTokenRepository purchasedTokenRepository;
    private final ICustomerRepository customerRepository;
    private final EmailService emailService;

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    @Transactional
    public void checkExpiringTokens() {

        log.info("📦 Calling stored procedure to generate expiring token notifications...");
        entityManager.createNativeQuery("CALL check_expiring_tokens_and_notify()").executeUpdate();

        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        List<Notification> newNotifications = notificationRepository.findByIssuedDateAfter(oneMinuteAgo);
        log.info("📨 Found {} new notifications to email", newNotifications.size());

        for (Notification notification : newNotifications) {
            String meterNumber = notification.getMeterNumber();

            customerRepository.findByMeters_MeterNumber(meterNumber).ifPresent(customer -> {
                String email = customer.getProfile().getEmail();
                String firstName = customer.getProfile().getFirstName();

                try {
                    Map<String, Object> vars = new HashMap<>();
                    vars.put("message", notification.getMessage());

                    emailService.sendEmail(
                            email,
                            firstName,
                            "Your Electricity Token is Expiring",
                            IEmailTemplate.NOTIFICATION,
                            vars
                    );

                    log.info("✅ Email sent to {} for meter {}", email, meterNumber);
                } catch (Exception e) {
                    log.error("❌ Failed to send email to {}: {}", email, e.getMessage());
                }
            });
    }

    }
}
