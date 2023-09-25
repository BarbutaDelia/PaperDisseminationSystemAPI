package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.User;
import ro.pds.PaperDisseminationSystem.exceptions.UserNotFound;
import ro.pds.PaperDisseminationSystem.repositories.UserRepository;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        if(userRepository.findById(id).isPresent())
            return userRepository.findById(id).get();
        else
            throw new UserNotFound(id);
    }

    public User getUserByEmail(String email) {
        if(userRepository.findByEmail(email).isPresent())
            return userRepository.findByEmail(email).get();
        else
            throw new UserNotFound(email);
    }

    public User getUserByName(String name) {
        if(userRepository.findByName(name).isPresent())
            return userRepository.findByName(name).get();
        else
            throw new UserNotFound(name);
    }

    public User getUserByMetamaskId(String metamaskId) {
        if(userRepository.findByMetamaskId(metamaskId).isPresent())
            return userRepository.findByMetamaskId(metamaskId).get();
        else
            throw new UserNotFound(metamaskId);
    }
}
