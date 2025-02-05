package bigezo.code.backend.service;

import bigezo.code.backend.model.Transaction;
import bigezo.code.backend.repository.TransactionRepository;
import bigezo.code.backend.model.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    //@Autowired
    //public TransactionService(TransactionRepository transactionRepository) {
     //   this.transactionRepository = transactionRepository;
    //}

    private final RestTemplate restTemplate;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.restTemplate = new RestTemplate(); // Initialize RestTemplate
    }


    public ResponseEntity<String> getPesapalTransactionStatus(String bearerToken, String trackingId) {
        String url = "https://pay.pesapal.com/v3/api/Transactions/GetTransactionStatus?orderTrackingId=" + trackingId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            System.out.println("Pesapal Response-------: " + response.getBody()); // Print response in JSON format
            return response;
        } catch (Exception e) {
            System.out.println("PESAPAL ERROR ----------------: " + e.getMessage()); // Log error in console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    public List<TransactionDto> getAllTransactions(Long schoolAdminId) {
        return transactionRepository.findBySchoolAdminId(schoolAdminId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<TransactionDto> getTransactionById(Long schoolAdminId, Long id) {
        return transactionRepository.findByIdAndSchoolAdminId(id, schoolAdminId)
                .map(this::mapToDto);
    }

    public TransactionDto createTransaction(Long schoolAdminId, Transaction transaction) {
        transaction.setSchoolAdminId(schoolAdminId);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDto(savedTransaction);
    }

    public TransactionDto updateTransaction(Long schoolAdminId, Long id, Transaction transaction) {
        if (!transactionRepository.existsById(id)) {
            return null;
        }
        transaction.setSchoolAdminId(schoolAdminId);
        transaction.setId(id);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToDto(updatedTransaction);
    }

    public void deleteTransaction(Long schoolAdminId, Long id) {
        transactionRepository.deleteById(id);
    }

    public Optional<TransactionDto> getTransactionByOrderTrackingId(String orderTrackingId) {
        return transactionRepository.findByOrderTrackingId(orderTrackingId)
                .map(this::mapToDto);
    }

    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();

        dto.setStudentId(transaction.getStudentId());
        dto.setStudentName(transaction.getStudentName());
        dto.setAmount(transaction.getAmount());
        dto.setOrderTrackingId(transaction.getOrderTrackingId());
        dto.setReason(transaction.getReason());
        dto.setDescription(transaction.getDescription());
        dto.setStatusCode(transaction.getStatusCode());
        dto.setStatus(transaction.getStatus());
        dto.setTime(transaction.getTime());
        dto.setLevel(transaction.getLevel());
        dto.setTerm(transaction.getTerm());
        dto.setYear(transaction.getYear());
        dto.setSchoolAdminId(transaction.getSchoolAdminId());
        return dto;
    }
}
