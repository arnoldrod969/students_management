package com.training.studies.repository;

import com.training.studies.entities.Payment;
import com.training.studies.entities.PaymentStatus;
import com.training.studies.entities.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    List<Payment> findByStudentCode(String code);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByType(PaymentType type);
}