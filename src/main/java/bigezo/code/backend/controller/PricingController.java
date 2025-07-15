package bigezo.code.backend.controller;

import bigezo.code.backend.model.Pricing;
import bigezo.code.backend.repository.PricingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    private final PricingRepository pricingRepository;

    public PricingController(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    @PostMapping
    public ResponseEntity<Pricing> setPricing(@RequestParam String tierName, @RequestParam BigDecimal costPerStudent, @RequestParam(required = false) String features) {
        Optional<Pricing> existingPricing = pricingRepository.findByTier(tierName);
        Pricing pricing;
        if (existingPricing.isPresent()) {
            pricing = existingPricing.get();
            pricing.setCostPerStudent(costPerStudent);
        } else {
            pricing = new Pricing();
            pricing.setTier(tierName); // Set the tier name
            pricing.setCostPerStudent(costPerStudent);
        }
        pricing.setFeatures(features); // Set the features
        pricingRepository.save(pricing);
        return ResponseEntity.ok(pricing);
    }

    @GetMapping
    public ResponseEntity<java.util.List<Pricing>> getAllPricingPackages() {
        return ResponseEntity.ok(pricingRepository.findAll());
    }
}
