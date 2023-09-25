package ro.pds.PaperDisseminationSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.pds.PaperDisseminationSystem.entities.Tag;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfTagsNotFound;
import ro.pds.PaperDisseminationSystem.exceptions.TagNotFound;
import ro.pds.PaperDisseminationSystem.repositories.TagRepository;

import java.util.List;

@Service
@Transactional
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> listAllTags() {
        if(!tagRepository.findAll().isEmpty())
            return tagRepository.findAll();
        else
            throw new CollectionOfTagsNotFound();
    }

    public Tag getTagById(Long id) {
        if(tagRepository.findById(id).isPresent())
            return tagRepository.findById(id).get();
        else
            throw new TagNotFound(id);
    }

    public Tag getTagByName(String name) {
        if(tagRepository.findByName(name).isPresent())
            return tagRepository.findByName(name).get();
        else
            throw new TagNotFound(name);
    }
}
