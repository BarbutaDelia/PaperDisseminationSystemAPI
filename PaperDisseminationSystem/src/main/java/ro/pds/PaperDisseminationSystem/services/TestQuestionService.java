package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.TestQuestion;
import ro.pds.PaperDisseminationSystem.exceptions.TestQuestionNotFound;
import ro.pds.PaperDisseminationSystem.repositories.TestQuestionRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TestQuestionService {
    @Autowired
    private TestQuestionRepository testQuestionRepository;

    public List<TestQuestion> listAllQuestionsByTagId(Long tagId) {
        Optional<List<TestQuestion>> optionalTestQuestions = testQuestionRepository.findByTestId(tagId);
        return optionalTestQuestions.orElse(Collections.emptyList());
    }

    public TestQuestion getQuestionById(Long id) {
        if(testQuestionRepository.findById(id).isPresent())
            return testQuestionRepository.findById(id).get();
        else
            throw new TestQuestionNotFound(id);
    }
}