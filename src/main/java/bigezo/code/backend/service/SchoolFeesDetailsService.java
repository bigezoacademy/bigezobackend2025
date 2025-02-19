package bigezo.code.backend.service;

import bigezo.code.backend.model.SchoolFeesDetails;
import bigezo.code.backend.repository.SchoolFeesDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolFeesDetailsService {

    @Autowired
    private SchoolFeesDetailsRepository repository;

    public List<SchoolFeesDetails> getAllDetails() {
        return repository.findAll();
    }

    public Optional<SchoolFeesDetails> getDetailsById(Long id) {
        return repository.findById(id);
    }

    public SchoolFeesDetails saveDetails(SchoolFeesDetails details) {
        return repository.save(details);
    }
    public List<SchoolFeesDetails> saveDetailsList(List<SchoolFeesDetails> detailsList) {
        return repository.saveAll(detailsList);
    }

    public List<SchoolFeesDetails> getDetailsByFeesId(Long feesId) {
        return repository.findBySchoolFeesSettingId(feesId);
    }

    public void deleteDetails(Long id) {
        repository.deleteById(id);
    }
}
