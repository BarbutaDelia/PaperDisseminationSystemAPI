package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.TagLevel;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfTagLevelsNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.TagLevelNotFound;
import ro.pds.PaperDisseminationSystem.repositories.TagLevelRepository;

import java.util.List;

@Service
@Transactional
public class TagLevelService {
    @Autowired
    private TagLevelRepository tagLevelRepository;

    public List<TagLevel> listAllTagLevels() {
        if(!tagLevelRepository.findAll().isEmpty())
            return tagLevelRepository.findAll();
        else
            throw new CollectionOfTagLevelsNotFound();
    }

    public TagLevel saveTagLevel(TagLevel tagLevel) {
        return tagLevelRepository.save(tagLevel);
    }

    public TagLevel getTagLevelByLevelIdAndTagId(Long tagId, Long levelId) {
        if(tagLevelRepository.findByTagIdAndLevelId(tagId, levelId).isPresent())
            return tagLevelRepository.findByTagIdAndLevelId(tagId, levelId).get();
        else
            throw new TagLevelNotFound(tagId, levelId);
    }

    public TagLevel getTagLevelByCID(String CID) {
        if(tagLevelRepository.findByCID(CID).isPresent())
            return tagLevelRepository.findByCID(CID).get();
        else
            throw new TagLevelNotFound(CID);
    }

}
