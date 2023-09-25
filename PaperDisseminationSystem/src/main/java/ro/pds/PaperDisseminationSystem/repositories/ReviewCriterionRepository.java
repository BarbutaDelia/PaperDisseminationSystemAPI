package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.ReviewCriterion;

@Repository
public interface ReviewCriterionRepository extends JpaRepository<ReviewCriterion, Long> {
}
