package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfUserTestsNotFound;
import ro.pds.PaperDisseminationSystem.entities.UserTest;
import ro.pds.PaperDisseminationSystem.repositories.UserTestRepository;

import java.util.List;

@Service
@Transactional
public class UserTestService {
    @Autowired
    private UserTestRepository userTestRepository;

    public UserTest getLatestUserTest(Long userId, Long tagId) {
        List<UserTest> userTests = userTestRepository.findLatestByUserIdAndTagId(userId, tagId);
        if (userTests.isEmpty()) {
            return null;
        } else {
            return userTests.get(0);
        }
    }

    public UserTest getLatestUserTestByUserIdAndTagId(Long userId, Long tagId) {
        List<UserTest> userTests = userTestRepository.findLatestByUserIdAndTagLevelId(userId, tagId);
        if (userTests.isEmpty()) {
            return null;
        } else {
            return userTests.get(0);
        }
    }

    public List<UserTest> getAllUserTestsByUserId(Long userId){
        if(!userTestRepository.findByUserId(userId).isEmpty())
            return userTestRepository.findByUserId(userId);
        else
            throw new CollectionOfUserTestsNotFound(userId);
    }

    public UserTest saveUserTest(UserTest userTest) {
        return userTestRepository.save(userTest);
    }
}

