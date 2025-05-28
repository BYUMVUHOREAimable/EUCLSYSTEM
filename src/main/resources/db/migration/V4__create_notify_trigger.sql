CREATE OR REPLACE PROCEDURE check_expiring_tokens_and_notify()
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO notifications (meter_number, message, issued_date)
    SELECT
        m.meter_number,
        'Dear Customer, REG is pleased to remind you that the token in meter ' || m.meter_number ||
        ' is going to expire in 5 hours. Please purchase a new token.',
        CURRENT_TIMESTAMP
    FROM purchased_tokens pt
             JOIN meters m ON m.id = pt.meter_id
    WHERE pt.status = 'NEW'
      AND pt.expiration_date IS NOT NULL
      AND pt.expiration_date > CURRENT_TIMESTAMP
      AND pt.expiration_date <= CURRENT_TIMESTAMP + INTERVAL '5 hours';
END;
$$;
