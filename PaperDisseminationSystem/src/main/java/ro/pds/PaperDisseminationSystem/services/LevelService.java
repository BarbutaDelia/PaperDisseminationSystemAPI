package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.Level;
import ro.pds.PaperDisseminationSystem.exceptions.LevelNotFound;
import ro.pds.PaperDisseminationSystem.repositories.LevelRepository;

@Service
@Transactional
public class LevelService {
    @Autowired
    private LevelRepository levelRepository;

    public Level getLevelById(Long id) {
        if(levelRepository.findById(id).isPresent())
            return levelRepository.findById(id).get();
        else
            throw new LevelNotFound(id);
    }

    public Level getLevelByName(String name) {
        if(levelRepository.findByName(name).isPresent()) {
            return levelRepository.findByName(name).get();
        }
        else {
            throw new LevelNotFound(name);
        }
    }
}