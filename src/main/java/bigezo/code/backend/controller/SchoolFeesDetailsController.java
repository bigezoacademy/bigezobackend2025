package bigezo.code.backend.controller;

import bigezo.code.backend.model.SchoolFeesDetails;
import bigezo.code.backend.service.SchoolFeesDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/school-fees-details")
public class SchoolFeesDetailsController {

    @Autowired
    private SchoolFeesDetailsService service;

    @GetMapping
    public List<SchoolFeesDetails> getAllDetails() {
        return service.getAllDetails();
    }



    @PostMapping
    public List<SchoolFeesDetails> createDetails(@RequestBody List<SchoolFeesDetails> detailsList) {
        return service.saveDetailsList(detailsList); // Assume service.saveDetailsList() handles saving a list
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetails(@PathVariable Long id) {
        service.deleteDetails(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-fees-id")
    public List<SchoolFeesDetails> getDetailsByFeesId(@RequestParam Long feesId) {
        return service.getDetailsByFeesId(feesId);
    }
}
