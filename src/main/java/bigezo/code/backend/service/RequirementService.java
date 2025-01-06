package bigezo.code.backend.service;


import bigezo.code.backend.SchoolAdminDto;
import bigezo.code.backend.model.Requirement;
import bigezo.code.backend.model.RequirementDto;
import bigezo.code.backend.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequirementService {

    private final RequirementRepository requirementRepository;

    @Autowired
    public RequirementService(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public List<RequirementDto> getAllRequirements() {
        return requirementRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<RequirementDto> getRequirementById(Long id) {
        return requirementRepository.findById(id).map(this::convertToDto);
    }

    public RequirementDto createRequirement(Requirement requirement) {
        Requirement savedRequirement = requirementRepository.save(requirement);
        return convertToDto(savedRequirement);
    }

    public RequirementDto updateRequirement(Long id, Requirement updatedRequirement) {
        return requirementRepository.findById(id).map(requirement -> {
            requirement.setItem(updatedRequirement.getItem());
            requirement.setDescription(updatedRequirement.getDescription());
            requirement.setUnitCost(updatedRequirement.getUnitCost());
            requirement.setLevel(updatedRequirement.getLevel());
            requirement.setTerm(updatedRequirement.getTerm());
            requirement.setYear(updatedRequirement.getYear());
            Requirement savedRequirement = requirementRepository.save(requirement);
            return convertToDto(savedRequirement);
        }).orElseThrow(() -> new IllegalArgumentException("Requirement not found with id: " + id));
    }

    public void deleteRequirement(Long id) {
        requirementRepository.deleteById(id);
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
                schoolAdminDto
        );
    }
}
