package com.project.back_end.models;

import java.sql.Date;



import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_id;

    @OneToOne
    @NotNull(message = "Appointment cannot be null")
    private Appointment appointment;

    @NotNull(message = "Amount cannot be null")
    private Double amount;

    @NotNull(message="Payment date cannot be null")
    private Date payment_date;

    private enum PaymentMethod {
        CARD,
        ONLINE,
        CASH
    }
    @NotNull(message = "Payment method cannot be null")
    @Enumerated(EnumType.STRING)
    private PaymentMethod payment_method;

    private enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
    @NotNull(message = "Payment status cannot be null")
    @Enumerated(EnumType.STRING)
    private PaymentStatus payment_status= PaymentStatus.PENDING;
}
