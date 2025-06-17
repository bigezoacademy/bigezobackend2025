package bigezo.code.backend.service;

import bigezo.code.backend.model.SchoolAdmin;
import bigezo.code.backend.dto.SchoolAdminDto;
import bigezo.code.backend.repository.SchoolAdminRepository;
import bigezo.code.backend.model.Requirement;
import bigezo.code.backend.model.RequirementDto;
import bigezo.code.backend.repository.RequirementRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequirementService {

    private static final Logger logger = LoggerFactory.getLogger(RequirementService.class);

    private final RequirementRepository requirementRepository;
    private final SchoolAdminRepository schoolAdminRepository;

    @Autowired
    public RequirementService(RequirementRepository requirementRepository, SchoolAdminRepository schoolAdminRepository) {
        this.requirementRepository = requirementRepository;
        this.schoolAdminRepository = schoolAdminRepository;
    }

    // Fetch requirements by school_admin_id, year, level, and term
    public List<RequirementDto> getRequirementsByFilters(Long schoolAdminId, int year, String level, int term) {
        List<Requirement> requirements = requirementRepository.findBySchoolAdminIdAndYearAndLevelAndTerm(schoolAdminId, year, level, term);
        return requirements.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RequirementDto> getAllRequirements(Long schoolAdminId) {
        return requirementRepository.findBySchoolAdminId(schoolAdminId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<RequirementDto> getRequirementById(Long schoolAdminId, Long id) {
        return requirementRepository.findByIdAndSchoolAdminId(id, schoolAdminId).map(this::convertToDto);
    }

    public RequirementDto createRequirement(Long schoolAdminId, Requirement requirement) {
        // Fetch the SchoolAdmin entity by schoolAdminId
        SchoolAdmin schoolAdmin = schoolAdminRepository.findById(schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("SchoolAdmin not found with id: " + schoolAdminId));

        // Set the SchoolAdmin entity in the Requirement
        requirement.setSchoolAdmin(schoolAdmin);

        Requirement savedRequirement = requirementRepository.save(requirement);
        return convertToDto(savedRequirement);
    }

    public RequirementDto updateRequirement(Long schoolAdminId, Long id, Requirement updatedRequirement) {
        return requirementRepository.findByIdAndSchoolAdminId(id, schoolAdminId).map(requirement -> {
            requirement.setItem(updatedRequirement.getItem());
            requirement.setDescription(updatedRequirement.getDescription());
            requirement.setUnitCost(updatedRequirement.getUnitCost());
            requirement.setLevel(updatedRequirement.getLevel());
            requirement.setTerm(updatedRequirement.getTerm());
            requirement.setYear(updatedRequirement.getYear());
            requirement.setQuantity(updatedRequirement.getQuantity());
            Requirement savedRequirement = requirementRepository.save(requirement);
            return convertToDto(savedRequirement);
        }).orElseThrow(() -> new IllegalArgumentException("Requirement not found with id: " + id));
    }

    @Transactional
    public void deleteRequirement(Long schoolAdminId, Long id) {
        try {
            // Log input parameters
            logger.debug("Deleting requirement with id: {} for schoolAdminId: {}", id, schoolAdminId);

            // First find the requirement to ensure it exists and to properly manage the entity state
            Optional<Requirement> requirement = requirementRepository.findByIdAndSchoolAdminId(id, schoolAdminId);
            
            if (!requirement.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Requirement not found for schoolAdminId: " + schoolAdminId + " and id: " + id);
            }

            // Log before deletion
            logger.debug("Requirement exists. Proceeding with deletion.");

            // Delete the requirement
            requirementRepository.deleteByIdAndSchoolAdminId(id, schoolAdminId);

            // Log after deletion
            logger.debug("Requirement with id: {} and schoolAdminId: {} deleted successfully.", id, schoolAdminId);
            
        } catch (Exception e) {
            logger.error("Error deleting requirement: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete requirement", e);
        }
    }

    private RequirementDto convertToDto(Requirement requirement) {
        SchoolAdminDto schoolAdminDto = new SchoolAdminDto(
                requirement.getSchoolAdmin().getId(),
                requirement.getSchoolAdmin().getSchoolName(),
                requirement.getSchoolAdmin().getDistrict()
        );

        return new RequirementDto(
                requirement.getId(),
                requirement.getItem(),
                requirement.getDescription(),
                requirement.getUnitCost(),
                requirement.getLevel(),
                requirement.getTerm(),
                requirement.getYear(),
                requirement.getQuantity(),
                schoolAdminDto
        );
    }
}
