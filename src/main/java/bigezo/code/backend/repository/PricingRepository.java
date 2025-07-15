package bigezo.code.backend.repository;

import bigezo.code.backend.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    Optional<Pricing> findByTier(String tier);
}
