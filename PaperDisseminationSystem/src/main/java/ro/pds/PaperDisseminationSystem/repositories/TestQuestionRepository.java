package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.TestQuestion;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {
    Optional<List<TestQuestion>> findByTestId(Long tagId);
}
