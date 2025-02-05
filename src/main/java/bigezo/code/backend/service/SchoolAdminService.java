package bigezo.code.backend.service;

import bigezo.code.backend.SchoolAdmin;
import bigezo.code.backend.SchoolAdminDto;
import bigezo.code.backend.SchoolAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolAdminService {

    private final SchoolAdminRepository repository;

    @Autowired
    public SchoolAdminService(SchoolAdminRepository repository) {
        this.repository = repository;
    }

    public List<SchoolAdminDto> getAllSchoolAdmins() {
        return repository.findAll().stream()
                .map(admin -> new SchoolAdminDto(admin.getId(), admin.getSchoolName(), admin.getDistrict()))
                .collect(Collectors.toList());
    }

    public SchoolAdminDto getSchoolAdminById(Long id) {
        SchoolAdmin admin = repository.findById(id).orElse(null);
        return admin != null ? new SchoolAdminDto(admin.getId(), admin.getSchoolName(), admin.getDistrict()) : null;
    }


    public void deleteSchoolAdmin(Long id) {
        repository.deleteById(id);
    }
}
