package bigezo.code.backend.service;


import bigezo.code.backend.model.SchoolFeesSetting;
import bigezo.code.backend.repository.SchoolFeesSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolFeesSettingService {

    @Autowired
    private SchoolFeesSettingRepository repository;

    public List<SchoolFeesSetting> getAllSettings() {
        return repository.findAll();
    }

    public Optional<SchoolFeesSetting> getSettingById(Long id) {
        return repository.findById(id);
    }

    public SchoolFeesSetting saveSetting(SchoolFeesSetting setting) {
        return repository.save(setting);
    }

    public void deleteSetting(Long id) {
        repository.deleteById(id);
    }
}
