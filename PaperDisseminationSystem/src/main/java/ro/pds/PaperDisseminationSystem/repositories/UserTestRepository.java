package ro.pds.PaperDisseminationSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.UserTest;

import java.util.List;

@Repository
public interface UserTestRepository extends JpaRepository<UserTest, Long> {
    @Query("SELECT ut FROM UserTest ut " +
            "WHERE ut.user.id = :userId " +
            "AND ut.tag.id = :tagId " +
            "ORDER BY ut.createdAt DESC")
    List<UserTest> findLatestByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId);

    @Query("SELECT ut FROM UserTest ut " +
            "WHERE ut.user.id = :userId " +
            "AND ut.tagLevel.id = :tagLevelId " +
            "ORDER BY ut.createdAt DESC")
    List<UserTest> findLatestByUserIdAndTagLevelId(@Param("userId") Long userId, @Param("tagLevelId") Long tagLevelId);
    List<UserTest> findByUserId(Long userId);

}
