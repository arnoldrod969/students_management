package com.training.studies.services;


import com.training.studies.entities.Payment;
import com.training.studies.entities.PaymentStatus;
import com.training.studies.entities.PaymentType;
import com.training.studies.entities.Student;
import com.training.studies.repository.PaymentRepository;
import com.training.studies.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private StudentRepository studentRepository;

    private PaymentRepository paymentRepository;

    public PaymentService(StudentRepository studentRepository, PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.paymentRepository = paymentRepository;
    }


    public Payment updatePaymentByStatus(PaymentStatus status , Long id){

        Payment payment = paymentRepository.findById(id).get();
        payment.setStatus(status);

        return paymentRepository.save(payment) ;

    }

    public byte[] getPaymentFile( Long id ) throws IOException {
        Payment payment = paymentRepository.findById(id).get();
        return Files.readAllBytes(Path.of(URI.create(payment.getFile())));
    }


    public Payment savePayment(MultipartFile file, LocalDate date, double amount,
                               PaymentType type, String studentCode) throws IOException {

        Path folderPath = Paths.get(System.getProperty("user.home"), "Desktop", "students_data", "payments");
        // Vérifier si le dossier existe, sinon le créer
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
            System.out.println("Dossier créé: " + folderPath);
        }
        String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(folderPath.toString(), fileName + ".pdf");
        Files.copy(file.getInputStream(), filePath);

        Student student = studentRepository.findByCode(studentCode);
        Payment payment = Payment.builder()
                .date(date).type(type)
                .student(student).amount(amount).file(filePath.toUri().toString())
                .status(PaymentStatus.CREATED)
                .build();

        return paymentRepository.save(payment);
    }

}
