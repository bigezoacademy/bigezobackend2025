package bigezo.code.backend.controller;

import bigezo.code.backend.model.SchoolFeesDetails;
import bigezo.code.backend.service.SchoolFeesDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/school-fees-details")
public class SchoolFeesDetailsController {

    @Autowired
    private SchoolFeesDetailsService service;


    @PostMapping
    public List<SchoolFeesDetails> createDetails(@RequestBody List<SchoolFeesDetails> detailsList) {
        return service.saveDetailsList(detailsList); // Assume service.saveDetailsList() handles saving a list
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDetails(@PathVariable Long id) {
        service.deleteDetails(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-fees-id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<SchoolFeesDetails> getDetailsByFeesId(@RequestParam Long feesId) {
        return service.getDetailsByFeesId(feesId);
    }
}
