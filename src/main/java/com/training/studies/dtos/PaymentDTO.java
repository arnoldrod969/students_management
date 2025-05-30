package com.training.studies.dtos;


import com.training.studies.entities.PaymentStatus;
import com.training.studies.entities.PaymentType;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @Builder
public class PaymentDTO {

    private Long id;

    private LocalDate date;

    private double amount;

    private PaymentType type;

    private PaymentStatus status ;
}
