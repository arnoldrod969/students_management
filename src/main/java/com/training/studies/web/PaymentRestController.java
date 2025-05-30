package com.training.studies.web;

import com.training.studies.entities.Payment;
import com.training.studies.entities.PaymentStatus;
import com.training.studies.entities.PaymentType;
import com.training.studies.entities.Student;
import com.training.studies.repository.PaymentRepository;
import com.training.studies.repository.StudentRepository;
import com.training.studies.services.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PaymentRestController {

    private StudentRepository studentRepository ;

    private PaymentRepository paymentRepository ;

    private PaymentService paymentService ;

    public PaymentRestController(StudentRepository studentRepository , PaymentRepository paymentRepository, PaymentService paymentService){
        this.studentRepository = studentRepository ;
        this.paymentRepository = paymentRepository ;
        this.paymentService    = paymentService;
    }

    @GetMapping(path = "/payments")
    public List<Payment> allPayments(){
        return paymentRepository.findAll() ;
    }

    @GetMapping(path = "/payments/{id}")
    public Optional<Payment> getPaymentById(@PathVariable Long id){
        return paymentRepository.findById(id);
    }

    @GetMapping(path = "/students/{code}/payments")
    public List<Payment> paymentsByStudent(@PathVariable String code ){
        return paymentRepository.findByStudentCode(code) ;
    }

    @GetMapping(path = "/payments/byStatus")
    public List<Payment> paymentsByStatus (@RequestParam PaymentStatus status){
        return paymentRepository.findByStatus(status) ;
    }

    @GetMapping(path = "/payments/byType")
    public List<Payment> paymentsByType (@RequestParam PaymentType type){
        return paymentRepository.findByType(type) ;
    }


    @GetMapping("/students")
    public List<Student> allStudents(){
        return studentRepository.findAll();
    }

    @GetMapping(path = "/students/{code}")
    public Student getStudentByCode(@PathVariable String code){
        return studentRepository.findByCode(code);
    }

    @GetMapping("/studentsByProgramId")
    public List<Student> getStudentsByProgramId(@RequestParam String programId ){
        return studentRepository.findByProgramId(programId) ;
    }


    @PutMapping(path = "/payments/{id}")
    public Payment updatePaymentByStatus(@RequestParam PaymentStatus status , @PathVariable Long id){

        return this.paymentService.updatePaymentByStatus(status, id) ;

    }

    @PostMapping(path = "/payments" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment savePayment(@RequestParam MultipartFile file , LocalDate date , double amount ,
                               PaymentType type , String studentCode) throws IOException {

       return this.paymentService.savePayment(file,date,amount,type,studentCode);

    }

    @GetMapping(path = "/paymentFile/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPaymentFile(@PathVariable Long id ) throws IOException {

        return this.paymentService.getPaymentFile(id);
    }

}
