package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.TagLevel;

import java.util.Optional;

@Repository
public interface TagLevelRepository extends JpaRepository<TagLevel, Long> {
    Optional <TagLevel> findByTagIdAndLevelId(Long tagId, Long levelId);
    Optional <TagLevel> findByCID(String CID);
}
