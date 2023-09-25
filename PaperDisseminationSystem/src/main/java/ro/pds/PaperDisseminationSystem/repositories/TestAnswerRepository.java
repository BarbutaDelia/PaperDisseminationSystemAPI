package ro.pds.PaperDisseminationSystem.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.TestAnswer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long> {
    Optional<List<TestAnswer>> findByQuestionId(Long questionId);
}
