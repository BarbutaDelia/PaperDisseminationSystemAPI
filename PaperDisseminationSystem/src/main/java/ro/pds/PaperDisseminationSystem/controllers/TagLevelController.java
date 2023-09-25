package ro.pds.PaperDisseminationSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pds.PaperDisseminationSystem.entities.TagLevel;
import ro.pds.PaperDisseminationSystem.exceptions.CollectionOfTagLevelsNotFound;
import ro.pds.PaperDisseminationSystem.services.LevelService;
import ro.pds.PaperDisseminationSystem.services.TagLevelService;
import ro.pds.PaperDisseminationSystem.services.TagService;
import ro.pds.PaperDisseminationSystem.view.response.TagLevelDto;

import java.util.ArrayList;
import java.util.List;

import static ro.pds.PaperDisseminationSystem.util.Conversion.getTagLevelDtosFromTagLevels;

@RestController
@RequestMapping("api/tagLevels")
public class TagLevelController {
    @Autowired
    private TagLevelService tagLevelService;

    @Autowired
    private TagService tagService;

    @Autowired
    private LevelService levelService;

    @GetMapping("")
    public ResponseEntity<?> getAllTagLevels() {
        try {
            List<TagLevel> tagLevels = tagLevelService.listAllTagLevels();
            List<Pair<String, String>> tagLevelNames = new ArrayList<>();

            for(TagLevel tagLevel: tagLevels) {
                String tagName = tagService.getTagById(tagLevel.getTag().getId()).getName();
                String levelName = levelService.getLevelById(tagLevel.getLevel().getId()).getName();
                tagLevelNames.add(Pair.of(tagName, levelName));
            }
            TagLevelDto tagLevelDtos = getTagLevelDtosFromTagLevels(tagLevelNames);
            return new ResponseEntity<>(tagLevelDtos, HttpStatus.OK);
        } catch (CollectionOfTagLevelsNotFound e) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }
}
