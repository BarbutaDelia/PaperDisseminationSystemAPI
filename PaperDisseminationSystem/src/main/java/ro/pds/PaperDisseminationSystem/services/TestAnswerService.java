package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.TestAnswer;
import ro.pds.PaperDisseminationSystem.exceptions.TestAnswerNotFound;
import ro.pds.PaperDisseminationSystem.repositories.TestAnswerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TestAnswerService {
    @Autowired
    private TestAnswerRepository testAnswerRepository;

    public List<TestAnswer> listAllAnswersByQuestionId(Long questionId) {
        Optional<List<TestAnswer>> optionalTestAnswers = testAnswerRepository.findByQuestionId(questionId);
        return optionalTestAnswers.orElse(Collections.emptyList());
    }

    public TestAnswer getAnswerById(Long id) {
        if(testAnswerRepository.findById(id).isPresent())
            return testAnswerRepository.findById(id).get();
        else
            throw new TestAnswerNotFound(id);
    }
}