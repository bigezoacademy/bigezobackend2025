package bigezo.code.backend.controller;

import bigezo.code.backend.model.SchoolFeesSetting;
import bigezo.code.backend.service.SchoolFeesSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/school-fees-settings")
public class SchoolFeesSettingController {

    @Autowired
    private SchoolFeesSettingService service;

    @GetMapping
    public List<SchoolFeesSetting> getAllSettings() {
        return service.getAllSettings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolFeesSetting> getSettingById(@PathVariable Long id) {
        Optional<SchoolFeesSetting> setting = service.getSettingById(id);
        return setting.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SchoolFeesSetting createSetting(@RequestBody SchoolFeesSetting setting) {
        return service.saveSetting(setting);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolFeesSetting> updateSetting(@PathVariable Long id, @RequestBody SchoolFeesSetting updatedSetting) {
        Optional<SchoolFeesSetting> existingSetting = service.getSettingById(id);

        if (existingSetting.isPresent()) {
            SchoolFeesSetting setting = existingSetting.get();
            setting.setReason(updatedSetting.getReason());
            setting.setLevel(updatedSetting.getLevel());
            setting.setTerm(updatedSetting.getTerm());
            setting.setYear(updatedSetting.getYear());
            setting.setTotal(updatedSetting.getTotal());
            setting.setTimestamp(updatedSetting.getTimestamp());
            setting.setSchoolAdmin(updatedSetting.getSchoolAdmin());

            SchoolFeesSetting savedSetting = service.saveSetting(setting);
            return ResponseEntity.ok(savedSetting);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        service.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }
}
