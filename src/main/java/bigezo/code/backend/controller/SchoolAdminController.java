package bigezo.code.backend.controller;


import bigezo.code.backend.SchoolAdminDto;
import bigezo.code.backend.service.SchoolAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school-admins")
public class SchoolAdminController {

    private final SchoolAdminService service;

    @Autowired
    public SchoolAdminController(SchoolAdminService service) {
        this.service = service;
    }

    @GetMapping
    public List<SchoolAdminDto> getAllSchoolAdmins() {
        return service.getAllSchoolAdmins();
    }

    @GetMapping("/{id}")
    public SchoolAdminDto getSchoolAdminById(@PathVariable Long id) {
        return service.getSchoolAdminById(id);
    }




    @DeleteMapping("/{id}")
    public void deleteSchoolAdmin(@PathVariable Long id) {
        service.deleteSchoolAdmin(id);
    }
}

