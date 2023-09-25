package ro.pds.PaperDisseminationSystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.pds.PaperDisseminationSystem.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByMetamaskId(String metamaskId);
    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByMetamaskId(String metamask_id);
}
