package bigezo.code.backend.service;

import bigezo.code.backend.SchoolAdmin;
import bigezo.code.backend.SchoolAdminDto;
import bigezo.code.backend.SchoolAdminRepository;
import bigezo.code.backend.model.Requirement;
import bigezo.code.backend.model.RequirementDto;
import bigezo.code.backend.repository.RequirementRepository;
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

    public void deleteRequirement(Long schoolAdminId, Long id) {
        // Log input parameters
        logger.debug("Deleting requirement with id: {} for schoolAdminId: {}", id, schoolAdminId);

        // Check if the requirement exists
        if (!requirementRepository.existsByIdAndSchoolAdminId(id, schoolAdminId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requirement not found for schoolAdminId: " + schoolAdminId + " and id: " + id);
        }

        // Log before deletion
        logger.debug("Requirement exists. Proceeding with deletion.");

        // Proceed with deletion
        requirementRepository.deleteByIdAndSchoolAdminId(id, schoolAdminId);

        // Log after deletion
        logger.debug("Requirement with id: {} and schoolAdminId: {} deleted successfully.", id, schoolAdminId);
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
