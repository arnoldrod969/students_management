package com.training.studies.web;

import com.training.studies.entities.Payment;
import com.training.studies.entities.PaymentStatus;
import com.training.studies.entities.PaymentType;
import com.training.studies.entities.Student;
import com.training.studies.repository.PaymentRepository;
import com.training.studies.repository.StudentRepository;
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

    public PaymentRestController( StudentRepository studentRepository , PaymentRepository paymentRepository ){
        this.studentRepository = studentRepository ;
        this.paymentRepository = paymentRepository ;
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

        Payment payment = paymentRepository.findById(id).get();
        payment.setStatus(status);

        return paymentRepository.save(payment) ;

    }

    @PostMapping(path = "/payments" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment savePayment(@RequestParam MultipartFile file , LocalDate date , double amount ,
                               PaymentType type , String studentCode) throws IOException {

        Path folderPath = Paths.get(System.getProperty("user.home"),"Desktop","students_data","payments");
        // Vérifier si le dossier existe, sinon le créer
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
            System.out.println("Dossier créé: " + folderPath);
        }
        String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(folderPath.toString() , fileName+".pdf") ;
        Files.copy(file.getInputStream(),filePath);

        Student student = studentRepository.findByCode(studentCode);
        Payment payment = Payment.builder()
                        .date(date).type(type)
                        .student(student).amount(amount).file(filePath.toUri().toString())
                        .status(PaymentStatus.CREATED)
                        .build();

        return paymentRepository.save(payment) ;
    }

    @GetMapping(path = "/paymentFile/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPaymentFile(@PathVariable Long id ) throws IOException {
        Payment payment = paymentRepository.findById(id).get();
        return Files.readAllBytes(Path.of(URI.create(payment.getFile())));
    }

}
