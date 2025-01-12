package bigezo.code.backend.controller;

import bigezo.code.backend.model.Requirement;
import bigezo.code.backend.model.RequirementDto;
import bigezo.code.backend.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
public class RequirementController {

    private final RequirementService requirementService;

    @Autowired
    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<RequirementDto>> getAllRequirements(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "level", required = false) String level,
            @RequestParam(name = "term", required = false) Integer term) {
        List<RequirementDto> requirements;
        if (year != null && level != null && term != null) {
            requirements = requirementService.getRequirementsByFilters(schoolAdminId, year, level, term);
        } else {
            requirements = requirementService.getAllRequirements(schoolAdminId);
        }
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RequirementDto> getRequirementById(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long id) {
        return requirementService.getRequirementById(schoolAdminId, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RequirementDto> createRequirement(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @RequestBody Requirement requirement) {
        RequirementDto createdRequirement = requirementService.createRequirement(schoolAdminId, requirement);
        return ResponseEntity.ok(createdRequirement);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RequirementDto> updateRequirement(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long id,
            @RequestBody Requirement requirement) {
        RequirementDto updatedRequirement = requirementService.updateRequirement(schoolAdminId, id, requirement);
        return ResponseEntity.ok(updatedRequirement);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRequirement(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long id) {
        requirementService.deleteRequirement(schoolAdminId, id);
        return ResponseEntity.noContent().build();
    }
}
